package com.finedustlab.controller;

import com.finedustlab.model.classroom.Classroom;
import com.finedustlab.model.classroom.ClassroomWrapper;
import com.finedustlab.model.user.UserProfile;
import com.finedustlab.service.ClassroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class ClassroomController {
    @Autowired
    ClassroomService classroomService;

    @PostMapping("/classroom/set")
    @Tag(name = "setClassroomStatus")
    @Operation(description = "현재 이용자가 속한 학급의 교내 미세먼지 상태를 업데이트합니다.")
    @ResponseBody
    public String setClassroom(@RequestBody ClassroomWrapper wrapper){
        Classroom classroom = wrapper.getClassroom();
        UserProfile userProfile = wrapper.getUserProfile();
        System.out.println("classroom.toString() = " + classroom.toString());
        System.out.println("userProfile.toString() = " + userProfile.toString());
        return classroomService.save(classroom, userProfile);
    }

    @GetMapping("/classroom/get")
    @Tag(name = "getClassroomStatus")
    @ResponseBody
    @Operation(description = "이용자 프로필을 읽어 교사 이용자가 업데이트한 학급의 미세먼지 상태를 가져옵니다.")
    public Object getClassroom(@RequestParam String schoolCode,
                                            @RequestParam String grade,
                                            @RequestParam String classNum) throws ExecutionException, InterruptedException {
        return classroomService.get(schoolCode, grade, classNum);
    }
}
