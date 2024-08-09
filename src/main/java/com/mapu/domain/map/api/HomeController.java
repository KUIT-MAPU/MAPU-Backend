package com.mapu.domain.map.api;

import com.mapu.domain.map.application.EditorService;
import com.mapu.domain.map.application.KeywordService;
import com.mapu.domain.map.application.response.EditorListResponseDTO;
import com.mapu.global.common.response.BaseResponse;
import com.mapu.global.jwt.dto.JwtUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final EditorService editorService;
    private final KeywordService keywordService;

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


    @GetMapping("/keyword")
    public BaseResponse<List<String>> getRecommendKeyword() {
        List<String> randomKeywords = keywordService.getRandomKeywords(5);
        return new BaseResponse<>(randomKeywords);
    }


    @GetMapping("/map")
    public BaseResponse<List> getMap(@AuthenticationPrincipal JwtUserDto jwtUserDto) {

        //로그인 사용자일 경우
        //팔로우한 사용자의 지도 조회



        //비 로그인 사용자일 경우
        //랜덤 사용자 지도 조회
        return null;
    }
}
