package com.mapu.domain.home.api;

import com.mapu.domain.home.EditorListResponseDTO;
import com.mapu.domain.home.EditorService;
import com.mapu.domain.home.application.HomeService;
import com.mapu.domain.home.application.response.HomeFollowingMapResponseDTO;
import com.mapu.domain.home.application.response.HomeKeywordMapResponseDTO;
import com.mapu.domain.keyword.application.KeywordService;
import com.mapu.global.common.response.BaseResponse;
import com.mapu.global.jwt.dto.JwtUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final EditorService editorService;
    private final KeywordService keywordService;
    private final HomeService homeService;

    /**
     * 랜덤 에디터 5개 조회
     */
    @GetMapping("/editor")
    public BaseResponse<List<EditorListResponseDTO>> getRecommendedEditors(@AuthenticationPrincipal JwtUserDto jwtUserDto) {
        List<EditorListResponseDTO> recommendedEditors;

        if (jwtUserDto != null) {
            // 로그인한 사용자의 경우
            log.info("로그인한 사용자의 경우");
            String userId = jwtUserDto.getName();
            recommendedEditors = editorService.getRecommendedEditors(Long.parseLong(userId), 5);
        } else {
            // 비로그인 사용자의 경우
            log.info("비로그인 사용자의 경우");
            recommendedEditors = editorService.getRandomEditors(5);
        }

        return new BaseResponse<>(recommendedEditors);
    }

    /**
     * 랜덤 키워드 5개 조회
     */
    @GetMapping("/keyword")
    public BaseResponse<List<String>> getRecommendKeyword() {
        List<String> randomKeywords = keywordService.getRandomKeywords(5);
        return new BaseResponse<>(randomKeywords);
    }

    /**
     *  자신이 팔로잉한 회원에 대한 지도 리스트 반환
     */
    @GetMapping("/map")
    public BaseResponse<List<HomeFollowingMapResponseDTO>> getFollowingMaps(@AuthenticationPrincipal JwtUserDto jwtUserDto) {
        Long userId = Long.parseLong(jwtUserDto.getName());
        List<HomeFollowingMapResponseDTO> followingMaps = homeService.getFollowingMaps(userId);
        return new BaseResponse<>(followingMaps);
    }

    /**
     *  키워드에 해당하는 지도 리스트반환(교집합이 아니라 키워드 각각에 해당하는 지도반환)
     */
    @GetMapping("/map/keyword")
    public BaseResponse<List<HomeKeywordMapResponseDTO>> getMapsByKeywords(@RequestParam List<String> keyword) {
        List<HomeKeywordMapResponseDTO> maps = homeService.getMapsByKeywords(keyword);
        return new BaseResponse<>(maps);
    }
}
