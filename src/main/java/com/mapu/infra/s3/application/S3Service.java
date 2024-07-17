package com.mapu.infra.s3.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mapu.infra.s3.exception.AwsS3ErrorCode;
import com.mapu.infra.s3.exception.AwsS3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {
    private static final Map<String, Boolean> S3_ALLOWED_IMAGE_FILE_TYPES = Map.of(
            "image/jpeg", true,
            "image/png", true,
            "image/gif", true
    );
    private final Pattern KEY_NAME_PATTERN = Pattern.compile("^https://.+\\.s3\\..+\\.amazonaws.com/(.+)");
    private static final long S3_MAX_IMAGE_FILE_SIZE = 10_000_000; //10MB
    private final AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile multipartFile, Long userId) throws IOException {
        //파일 유효성 검증
        validateImageFileType(multipartFile.getContentType());
        validateImageFileSize(multipartFile.getSize());

        //파일 이름 생성 및 S3에 업로드
        String fileName = generateFileName(multipartFile.getOriginalFilename(), userId);
        uploadToS3Bucket(multipartFile, fileName);

        //이미지 URL 반환
        String imageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        log.info("S3 파일 업로드에 성공. URL: {}", imageUrl);

        return imageUrl;
    }

    public void deleteImage(String imageUrl){
        String keyName = getKeyNameFromFileUrl(imageUrl);
        deleteObject(keyName);
    }

    private void deleteObject(String keyName) {
        try{
            amazonS3Client.deleteObject(bucket,keyName);
        } catch (Exception e){
            log.error("S3 파일 삭제에 실패했습니다. {}", e.getMessage());
            throw new AwsS3Exception(AwsS3ErrorCode.AWS_S3_IMAGE_DELETE_FAIL);
        }
    }

    private String getKeyNameFromFileUrl(String imageUrl) {
        Matcher matcher = KEY_NAME_PATTERN.matcher(imageUrl);
        if(!matcher.matches()){
            log.error("파일 삭제 요청 URL이 올바르지 않습니다.");
            throw new AwsS3Exception(AwsS3ErrorCode.AWS_S3_INVALID_FILE_URL);
        }
        String keyName = matcher.group(1);
        //띄어쓰기가 포함된 파일 이름의 경우 +를 띄어쓰기로 대체해줘야 삭제된다.
        return keyName.replace('+',' ');
    }

    private void validateImageFileSize(long size) {
        if(size > S3_MAX_IMAGE_FILE_SIZE){ // 10MB
            throw new AwsS3Exception(AwsS3ErrorCode.AWS_S3_FILE_TOO_LARGE);
        }
    }

    private void validateImageFileType(String contentType) {
        if(!S3_ALLOWED_IMAGE_FILE_TYPES.containsKey(contentType)){
            throw new AwsS3Exception(AwsS3ErrorCode.AWS_S3_INVALID_FILE_TYPE);
        }
    }

    private void uploadToS3Bucket(MultipartFile multipartFile, String fileName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        try(InputStream inputStream = multipartFile.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }catch (IOException e){
            log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage());
            throw new AwsS3Exception(AwsS3ErrorCode.AWS_S3_IMAGE_UPLOAD_FAIL);
        }
    }

    private String generateFileName(String originalFilename, Long userId) {
        return userId+"/"+originalFilename+"/"+ LocalDateTime.now();
    }


}
