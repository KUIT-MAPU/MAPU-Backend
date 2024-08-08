package com.mapu.domain.map.application;

import com.mapu.domain.map.api.request.CreateMapRequestDTO;
import com.mapu.domain.map.application.response.MapEditorListResponseDTO;
import com.mapu.domain.map.application.response.MapEditorResponseDTO;
import com.mapu.domain.map.application.response.MapListResponseDTO;
import com.mapu.domain.map.application.response.MapOwnerResponseDTO;
import com.mapu.domain.map.dao.MapKeywordRepository;
import com.mapu.domain.map.dao.MapUserBookmarkRepository;
import com.mapu.domain.map.dao.MapRepository;
import com.mapu.domain.map.dao.MapUserRoleRepository;
import com.mapu.domain.map.domain.Map;
import com.mapu.domain.map.domain.MapUserRole;
import com.mapu.domain.map.domain.Role;
import com.mapu.domain.map.domain.MapUserBookmark;
import com.mapu.domain.map.exception.MapException;
import com.mapu.domain.map.exception.errcode.MapExceptionErrorCode;
import com.mapu.domain.user.dao.UserRepository;
import com.mapu.domain.user.domain.User;
import com.mapu.domain.user.exception.UserException;
import com.mapu.domain.user.exception.errorcode.UserExceptionErrorCode;
import com.mapu.domain.map.exception.errcode.MapExceptionErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MapService {
    private final MapRepository mapRepository;
    private final MapKeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final MapUserRoleRepository mapUserRoleRepository;
    private final MapUserBookmarkRepository mapUserBookmarkRepository;

    public void checkLoginStatus(HttpServletRequest request) {
    }

    public List<MapListResponseDTO> getMapList(String searchType, Pageable pageable) {
        switch (searchType) {
            case "RANDOM": {
                List<MapListResponseDTO> mapList = getMapListByRandom(pageable);
                return mapList;
            }
            case "DATE": {
                List<MapListResponseDTO> mapList = getMapListByDate(pageable);
                return mapList;
            }
            default:
                throw new MapException(MapExceptionErrorCode.SOCIALTYPE_ERROR);
        }
    }

    private List<MapListResponseDTO> getMapListByDate(Pageable pageable) {
        List<Map> maps = mapRepository.findAllByOrderByCreatedAtDesc(pageable);
        log.info("MapService GetMapListByDate - Retrieved {} map(s) from the database", maps.size());
        return maps.stream().map(this::mapConvertToDTO).collect(Collectors.toList());
    }

    private List<MapListResponseDTO> getMapListByRandom(Pageable pageable) {
        List<Map> maps = mapRepository.findAllByRandom(pageable);
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
                .imageUrl(map.getPublishLink())
                .user(mapOwnerDTO)
                .keyword(keywords)
                .build();
    }

    public void addMapBookmark(long userId, Long mapId) {
        User user = userRepository.findById(userId);
        Map map = mapRepository.findById(mapId).orElseThrow(()-> new MapException(MapExceptionErrorCode.NOT_FOUND_MAP));
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

    public MapEditorListResponseDTO getEditorList(long mapId, int pageNumber, int pageSize) {
        Map map = mapRepository.findById(mapId);
        if (map==null) throw new MapException(MapExceptionErrorCode.NO_EXIST_MAP);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<MapEditorResponseDTO> page = mapUserRoleRepository.findMapEditor(mapId,pageRequest);
        MapEditorListResponseDTO response = MapEditorListResponseDTO.builder()
                .mapEditors(page.getContent())
                .totalPageCount(page.getTotalPages())
                .build();

        return response;
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
                .user(user)
                .build();
        mapRepository.save(map);
    }

    public void addEditor(long mapId, String nickname) {
        User user = userRepository.findByNickname(nickname);
        if (user==null) throw new UserException(UserExceptionErrorCode.INVALID_NICKNAME);

        Map map = mapRepository.findById(mapId);
        if (map==null) throw new MapException(MapExceptionErrorCode.NO_EXIST_MAP);

        if(mapUserRoleRepository.existsByMapIdAndUserId(map.getId(),user.getId())){
            throw new MapException(MapExceptionErrorCode.ALREADY_EDITOR);
        }

        MapUserRole mapUserRole = MapUserRole.builder()
                .role(Role.EDITOR)
                .user(user).map(map)
                .build();

        mapUserRoleRepository.save(mapUserRole);
    }

    public List<MapListResponseDTO> getOtherUserMapList(long otherUserId, Pageable pageable) {
        List<Map> maps = mapRepository.findOtherUserMapsByUserId(otherUserId, pageable);
        log.info("MapService getOtherUserMapList - Retrieved {} map(s) from the database", maps.size());
        return maps.stream().map(this::mapConvertToDTO).collect(Collectors.toList());

    }
}
