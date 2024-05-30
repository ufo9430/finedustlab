package com.finedustlab.model.classroom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(description = "교실 상태 응답 DTO")
public class ClassroomResponseDTO {
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
