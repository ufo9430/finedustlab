package com.finedustlab.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "날씨 api 요청 dto")
public class WeatherRequestDTO {
    @Schema(description = "위도", defaultValue = "37.582425")
    private String lat;

    @Schema(description = "경도", defaultValue = "127.582425")
    private String lng;

}
