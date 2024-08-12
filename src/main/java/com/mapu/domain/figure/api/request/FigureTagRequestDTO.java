package com.mapu.domain.figure.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FigureTagRequestDTO {
    @NotNull(message = "tag값은 필수입니다.")
    private String tag;
}
