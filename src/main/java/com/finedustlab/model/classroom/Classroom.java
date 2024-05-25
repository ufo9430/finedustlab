package com.finedustlab.model.classroom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(description = "교실 상태 DTO")
public class Classroom {
    @Schema(description = "미세먼지 지수", defaultValue = "0")
    private int finedust_factor;
    @Schema(description = "초미세먼지 지수", defaultValue = "0")
    private int ultrafine_factor;
}
