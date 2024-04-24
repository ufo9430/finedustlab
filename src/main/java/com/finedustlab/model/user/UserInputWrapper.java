package com.finedustlab.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserInputWrapper {
    private TeacherProfile userProfile;
    private String uid;
}
