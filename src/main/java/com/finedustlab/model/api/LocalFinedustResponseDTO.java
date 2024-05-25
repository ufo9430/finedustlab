package com.finedustlab.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "미세먼지 api 응답 dto")
public class LocalFinedustResponseDTO {
    @Schema(description = "미세먼지 지수", defaultValue = "20")
    private String finedust_factor;
    @Schema(description = "초미세먼지 지수", defaultValue = "13")
    private String ultrafine_factor;
    @Schema(description = "미세먼지 상태", defaultValue = "good", allowableValues = {"good","fine","bad"})
    private String fine_status;
    @Schema(description = "초미세먼지 상태", defaultValue = "good", allowableValues = {"good","fine","bad"})
    private String ultra_status;
    @Schema(description = "호출 결과", allowableValues = {"complete","error"})
    private String result;
}
