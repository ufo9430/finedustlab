package com.finedustlab.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeacherProfile extends UserProfile{
    @Schema(description = "선생님 코드")
    private String teacher_code;
    @Schema(description = "이용자 학교 이름")
    private String school_name;
    @Schema(description = "이용자 학교 주소")
    private String school_address;
}
