package com.mapu.domain.map.application;

import com.mapu.domain.map.application.response.MapEditorListResponseDTO;
import com.mapu.domain.map.application.response.MapEditorResponseDTO;
import com.mapu.domain.map.dao.MapRepository;
import com.mapu.domain.map.dao.MapUserRoleRepository;
import com.mapu.domain.map.domain.Map;
import com.mapu.domain.map.domain.MapUserRole;
import com.mapu.domain.map.domain.Role;
import com.mapu.domain.map.exception.MapException;
import com.mapu.domain.map.exception.errcode.MapExceptionErrorCode;
import com.mapu.domain.user.dao.UserRepository;
import com.mapu.domain.user.domain.User;
import com.mapu.domain.user.exception.UserException;
import com.mapu.domain.user.exception.errorcode.UserExceptionErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MapUserRoleService {
    private final MapUserRoleRepository mapUserRoleRepository;
    private final MapRepository mapRepository;
    private final UserRepository userRepository;

    public boolean hasAuthority(long mapId, long userId) {
        return mapUserRoleRepository.existsByMapIdAndUserId(mapId, userId);
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

    public void addRole(long mapId, String nickname, Role role) {
        User user = userRepository.findByNickname(nickname);
        if (user==null) throw new UserException(UserExceptionErrorCode.INVALID_NICKNAME);

        Map map = mapRepository.findById(mapId);
        if (map==null) throw new MapException(MapExceptionErrorCode.NO_EXIST_MAP);

        if(hasAuthority(map.getId(),user.getId())){
            throw new MapException(MapExceptionErrorCode.ALREADY_EDITOR);
        }

        MapUserRole mapUserRole = MapUserRole.builder()
                .role(role)
                .user(user).map(map)
                .build();

        mapUserRoleRepository.save(mapUserRole);
    }

    public void addOwner(long mapId, String nickname) {
        addRole(mapId, nickname, Role.OWNER);
    }

    public void addEditor(long mapId, String nickname) {
        addRole(mapId, nickname, Role.EDITOR);
    }
}
