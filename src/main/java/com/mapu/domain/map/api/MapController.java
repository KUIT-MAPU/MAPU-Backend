package com.mapu.domain.map.api;

import com.mapu.domain.map.application.MapService;
import com.mapu.domain.map.application.response.MapListResponseDTO;
import com.mapu.global.common.response.BaseResponse;
import com.mapu.global.jwt.dto.JwtUserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    /**
     * 탐색화면 로그인 여부 확인
     */
    @GetMapping("/logined")
    public BaseResponse checkLoginStatus(HttpServletRequest request) {
        mapService.checkLoginStatus(request);
        return new BaseResponse<>();
    }

    /**
     * 탐색화면 지도 리스트 조회 (랜덤 or 날짜)
     */
    @GetMapping("/search")
    public BaseResponse<List<MapListResponseDTO>> getMapList(
            @RequestParam("searchType") String searchType,
            final Pageable pageable) {

        log.info("MapController searchType: {}", searchType);
        List<MapListResponseDTO> responseDTOList = mapService.getMapList(searchType.toUpperCase(), pageable);
        log.info("MapController getMapList - responseDTOList size: {}", responseDTOList.size());
        return new BaseResponse<>(responseDTOList);
    }

    /**
     * 탐색화면 북마크 추가
     */
    @PostMapping("/bookmark")
    public BaseResponse addMapBookmark(@AuthenticationPrincipal JwtUserDto jwtUserDto, @RequestParam Long mapId) {
        mapService.addMapBookmark(Long.parseLong(jwtUserDto.getName()), mapId);
        return new BaseResponse<>();
    }

    /**
     * 탐색화면 북마크 취소
     */
    @DeleteMapping("/bookmark")
    public BaseResponse removeMapBookmark(@AuthenticationPrincipal JwtUserDto jwtUserDto, @RequestParam Long mapId) {
        mapService.removeMapBookmark(Long.parseLong(jwtUserDto.getName()), mapId);
        return new BaseResponse<>();
    }

}
