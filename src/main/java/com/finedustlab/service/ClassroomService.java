package com.finedustlab.service;

import com.finedustlab.domain.repository.ClassroomRepository;
import com.finedustlab.model.classroom.Classroom;
import com.finedustlab.model.user.StudentProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ClassroomService {
    @Autowired
    ClassroomRepository classroomRepository;

    public String save(Classroom classroom, StudentProfile userProfile){
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

    public Classroom get(String schoolCode, String grade, String classNum) throws ExecutionException, InterruptedException {
        Map<String, Object> data = classroomRepository.findBySchoolInfo(schoolCode, grade, classNum);
        Classroom classroom = new Classroom();
        classroom.setFinedust_factor(Integer.parseInt(data.get("finedust_factor").toString()));
        classroom.setUltrafine_factor(Integer.parseInt(data.get("ultrafine_factor").toString()));
        return classroom;
    }

}
