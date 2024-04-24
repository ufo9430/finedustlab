package com.finedustlab.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAccount{
    @Schema(description = "이용자 이메일")
    private String userEmail;
    @Schema(description = "이용자 패스워드")
    private String userPassword;
}
