package com.finedustlab.controller;

import com.finedustlab.model.classroom.Classroom;
import com.finedustlab.model.user.UserProfile;
import com.finedustlab.service.ClassroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClassroomController {
    @Autowired
    ClassroomService classroomService;

    @PostMapping("/classroom/set")
    @Tag(name = "setClassroomStatus")
    @Operation(description = "교내 미세먼지 상태를 업데이트합니다. 작업중")
    @ResponseBody
    public String setClassroom(@RequestBody Classroom classroom, @RequestBody UserProfile userProfile){
        return classroomService.save(classroom, userProfile);
    }

}
