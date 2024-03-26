package com.finedustlab.service;

import com.finedustlab.domain.repository.ClassroomRepository;
import com.finedustlab.model.classroom.Classroom;
import com.finedustlab.model.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassroomService {
    @Autowired
    ClassroomRepository classroomRepository;

    public String save(Classroom classroom, UserProfile userProfile){
        if(userProfile == null){
            return "error";
        }else{
            classroomRepository.save(userProfile.getSchool_code(),
                    userProfile.getGrade(),
                    userProfile.getClass_num(),
                    classroom);
        }
        return "complete";
    }

}
