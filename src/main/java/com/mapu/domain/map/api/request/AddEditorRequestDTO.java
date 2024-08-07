package com.mapu.domain.map.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddEditorRequestDTO {
    @NotNull(message = "nickname은 필수입니다.")
    String nickname;
}
