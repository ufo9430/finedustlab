package com.finedustlab.model.classroom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(description = "교실 상태 DTO")
public class ClassroomRequestDTO {
    @Schema(description = "미세먼지 지수", defaultValue = "20")
    private String finedust_factor;
    @Schema(description = "초미세먼지 지수", defaultValue = "13")
    private String ultrafine_factor;
}
