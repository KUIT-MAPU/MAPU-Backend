package com.mapu.domain.map.application;

import com.mapu.domain.map.api.request.CreateMapRequestDTO;
import com.mapu.domain.map.application.response.MapListResponseDTO;
import com.mapu.domain.map.application.response.MapOwnerResponseDTO;
import com.mapu.domain.map.dao.MapKeywordRepository;
import com.mapu.domain.map.dao.MapUserBookmarkRepository;
import com.mapu.domain.map.dao.MapRepository;
import com.mapu.domain.map.domain.Map;
import com.mapu.domain.map.domain.MapUserBookmark;
import com.mapu.domain.map.exception.MapException;
import com.mapu.domain.map.exception.errcode.MapExceptionErrorCode;
import com.mapu.domain.user.dao.UserRepository;
import com.mapu.domain.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final MapRepository mapRepository;
    private final MapKeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final MapUserBookmarkRepository mapUserBookmarkRepository;
    private final MapUserRoleService mapUserRoleService;


    public void checkLoginStatus(HttpServletRequest request) {
    }

    public List<MapListResponseDTO> getMapList(String searchType, Pageable pageable, String searchWord) {
        switch (searchType) {
            case "RANDOM": {
                List<MapListResponseDTO> mapList = getMapListByRandom(pageable, searchWord);
                return mapList;
            }
            case "DATE": {
                List<MapListResponseDTO> mapList = getMapListByDate(pageable, searchWord);
                return mapList;
            }
            default:
                throw new MapException(MapExceptionErrorCode.SOCIALTYPE_ERROR);
        }
    }

    private List<MapListResponseDTO> getMapListByDate(Pageable pageable, String searchWord) {
        List<Map> maps = mapRepository.findAllByOrderByCreatedAtDesc(searchWord,pageable);
        log.info("MapService GetMapListByDate - Retrieved {} map(s) from the database", maps.size());
        return maps.stream().map(this::mapConvertToDTO).collect(Collectors.toList());
    }

    private List<MapListResponseDTO> getMapListByRandom(Pageable pageable, String searchWord) {
        List<Map> maps = mapRepository.findAllByRandom(searchWord, pageable);
        log.info("MapService GetMapListByRandom - Retrieved {} map(s) from the database", maps.size());
        return maps.stream().map(this::mapConvertToDTO).collect(Collectors.toList());
    }

    private MapListResponseDTO mapConvertToDTO(Map map) {

        MapOwnerResponseDTO mapOwnerDTO = null;
        User user = map.getUser();
        if(user != null) {
            mapOwnerDTO = MapOwnerResponseDTO.builder()
                    .imageUrl(user.getImage())
                    .nickName(user.getNickname())
                    .profileId(user.getProfileId())
                    .build();
        }

        List<String> keywords = keywordRepository.findKeywordsByMapId(map.getId());
        log.info("MapService mapConvertToDTO - Retrieved keywords from the database");
        log.info("keywords = " + keywords);

        return MapListResponseDTO.builder()
                .title(map.getMapTitle())
                .region(map.getAddress())
                .description(map.getMapDescription())
                .imageUrl(map.getImageUrl())
                .user(mapOwnerDTO)
                .keyword(keywords)
                .build();
    }

    public void addMapBookmark(long userId, Long mapId) {
        User user = userRepository.findById(userId);
        Map map = mapRepository.findById(mapId).orElseThrow(()-> new MapException(MapExceptionErrorCode.NO_EXIST_MAP));
        MapUserBookmark mapUserBookmark = MapUserBookmark.builder().user(user).map(map).build();
        mapUserBookmarkRepository.save(mapUserBookmark);
    }

    public void removeMapBookmark(long userId, Long mapId) {
        MapUserBookmark mapUserBookmark = mapUserBookmarkRepository.findByUserIdAndMapId(userId, mapId);
        if (mapUserBookmark == null){
            throw new MapException(MapExceptionErrorCode.NOT_FOUND_BOOKMARK);
        }
        mapUserBookmarkRepository.delete(mapUserBookmark);
    }


    public void createMap(CreateMapRequestDTO requestDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저 없음"));

        Map map = Map.builder()
                .mapTitle(requestDTO.getMapTitle())
                .mapDescription(requestDTO.getMapDescription())
                .address(requestDTO.getAddress())
                .latitude(requestDTO.getLatitude())
                .longitude(requestDTO.getLongitude())
                .zoomLevel(requestDTO.getZoomLevel())
                .publishLink(requestDTO.getPublishLink())
                .isOnSearch(requestDTO.getIsOnSearch())
                .imageUrl(requestDTO.getImageUrl())
                .user(user)
                .build();
        mapRepository.save(map);
        mapUserRoleService.addOwner(map.getId(), user.getNickname());
    }

    public List<MapListResponseDTO> getOtherUserMapList(long otherUserId, Pageable pageable) {
        List<Map> maps = mapRepository.findOtherUserMapsByUserId(otherUserId, pageable);
        log.info("MapService getOtherUserMapList - Retrieved {} map(s) from the database", maps.size());
        return maps.stream().map(this::mapConvertToDTO).collect(Collectors.toList());

    }
}
