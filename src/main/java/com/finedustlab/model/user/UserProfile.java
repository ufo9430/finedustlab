package com.finedustlab.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserProfile {
    private int school_code;
    private int grade;
    private int class_num;
    private String name;
}
