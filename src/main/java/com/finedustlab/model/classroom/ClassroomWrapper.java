package com.finedustlab.model.classroom;

import com.finedustlab.model.user.TeacherProfile;
import com.finedustlab.model.user.UserProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassroomWrapper {
    private ClassroomRequestDTO classroom;
    private UserProfile userProfile;
}
