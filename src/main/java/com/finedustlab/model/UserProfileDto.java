package com.finedustlab.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserProfileDto {
    private int school_code;
    private int grade;
    private int class_num;
    private String name;
    //doc_id = timestamp+"-"+school_code+"-"+grade+"-"+class_num+"-"+student_id
}
