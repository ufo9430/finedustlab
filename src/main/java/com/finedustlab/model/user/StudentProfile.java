package com.finedustlab.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StudentProfile extends UserProfile{
    @Schema(description = "이용자 반 번호")
    private int student_num;
}