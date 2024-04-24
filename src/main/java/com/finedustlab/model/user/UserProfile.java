package com.finedustlab.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfile {
    @Schema(description = "이용자 학교 코드")
    private int school_code;
    @Schema(description = "이용자 소속 학년")
    private int grade;
    @Schema(description = "이용자 소속 반")
    private int class_num;
    @Schema(description = "이용자 이름")
    private String name;
    @Schema(description = "사용자 타입")
    private String user_type;
}
