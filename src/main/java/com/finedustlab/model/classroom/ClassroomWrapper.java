package com.finedustlab.model.classroom;

import com.finedustlab.model.user.StudentProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassroomWrapper {
    private Classroom classroom;
    private StudentProfile userProfile;
}
