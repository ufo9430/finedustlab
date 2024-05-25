package com.finedustlab.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "날씨 api 응답 dto")
public class WeatherResponseDTO {
    @Schema(description = "데이터 아이디(중복 확인용)", defaultValue = "70-127-1030")
    private String id;
    @Schema(description = "습도", defaultValue = "50")
    private String humidity;
    @Schema(description = "온도", defaultValue = "20")
    private String temperature;
    @Schema(description = "날짜", defaultValue = "20240525-1126")
    private String date;
    @Schema(description = "호출 결과", allowableValues = {"complete","error"})
    private String result;
}
