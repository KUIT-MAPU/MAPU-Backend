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
public class FigureRateRequestDTO {
    private String rateName;
    private int rateStar;

    @NotNull(message = "Rate의 이름 필요")
    public String getRateName() {
        return rateName;
    }

    @NotNull(message = "Rate의 값 명시")
    public int getRateStar() {
        return rateStar;
    }
}