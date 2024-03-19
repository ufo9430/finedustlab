package com.finedustlab.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserAccountDto {
    private String email;
    private String password;
    private String user_type;
}
