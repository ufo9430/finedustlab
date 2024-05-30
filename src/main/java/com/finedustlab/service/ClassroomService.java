package com.finedustlab.service;

import com.finedustlab.domain.repository.ClassroomRepository;
import com.finedustlab.model.classroom.ClassroomRequestDTO;
import com.finedustlab.model.classroom.ClassroomResponseDTO;
import com.finedustlab.model.user.TeacherProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ClassroomService {
    @Autowired
    ClassroomRepository classroomRepository;

    public String save(ClassroomRequestDTO classroom, TeacherProfile userProfile){
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

    public ClassroomResponseDTO get(String schoolCode, String grade, String classNum) throws ExecutionException, InterruptedException {
        Map<String, Object> data = classroomRepository.findBySchoolInfo(schoolCode, grade, classNum);
        ClassroomResponseDTO classroom = new ClassroomResponseDTO();
        classroom.setFinedust_factor(data.get("finedust_factor").toString());
        classroom.setUltrafine_factor(data.get("ultrafine_factor").toString());
        classroom.setFine_status(data.get("fine_status").toString());
        classroom.setUltra_status(data.get("ultra_status").toString());
        classroom.setResult(data.get("result").toString());
        return classroom;
    }

}
