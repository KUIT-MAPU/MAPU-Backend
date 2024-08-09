package com.mapu.domain.map.application.response;

import com.mapu.domain.map.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MapEditorResponseDTO {
    Long userId;
    String nickname;
    Role role;
    String image;
}
