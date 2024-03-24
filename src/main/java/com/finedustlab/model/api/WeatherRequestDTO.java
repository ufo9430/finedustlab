package com.finedustlab.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "날씨 api 요청 dto")
public class WeatherRequestDTO {
    @Schema(description = "위도", defaultValue = "37.582425")
    private String lat;

    @Schema(description = "경도", defaultValue = "127.582425")
    private String lng;

    @Schema(description = "날짜 및 시간", defaultValue = "20240324-2329")
    private String date;

}
