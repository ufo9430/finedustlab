package com.finedustlab.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserAccount {
    private String email;
    private String password;
    private String user_type;
}