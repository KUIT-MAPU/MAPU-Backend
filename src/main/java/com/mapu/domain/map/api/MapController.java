package com.mapu.domain.map.api;

import com.mapu.domain.map.application.MapService;
import com.mapu.domain.map.application.response.MapListResponseDTO;
import com.mapu.global.common.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/Logined")
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
            @RequestParam("page") int page,
            @RequestParam("rows") int rows) {

        log.info("MapController getMapList - searchType: {}, page: {}, rows: {}", searchType, page, rows);
        List<MapListResponseDTO> responseDTOList = mapService.getMapList(searchType.toUpperCase(), page, rows);
        log.info("MapController getMapList - responseDTOList size: {}", responseDTOList.size());
        return new BaseResponse<>(responseDTOList);
    }

    /**
     * 탐색화면 북마크 추가
     */
    public BaseResponse addMapBookMark() {
        return new BaseResponse<>();
    }

    /**
     * 탐색화면 북마크 취소
     */
    public BaseResponse removeMapBookMark() {
        return new BaseResponse<>();
    }

}
