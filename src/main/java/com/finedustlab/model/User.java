package com.finedustlab.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class User {
    private String email;
    private String password;
    private String user_type;
    private String school_code;
}
