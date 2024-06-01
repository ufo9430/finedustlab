package com.finedustlab.controller;

import com.finedustlab.model.classroom.ClassroomRequestDTO;
import com.finedustlab.model.classroom.ClassroomResponseDTO;
import com.finedustlab.model.classroom.ClassroomWrapper;
import com.finedustlab.model.user.TeacherProfile;
import com.finedustlab.service.ClassroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        ClassroomRequestDTO classroom = wrapper.getClassroom();
        TeacherProfile userProfile = wrapper.getUserProfile();
        return classroomService.save(classroom, userProfile);
    }

    @GetMapping("/classroom/get")
    @Tag(name = "getClassroomStatus")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학급 정보 불러오기 성공", content = @Content(schema = @Schema(implementation = ClassroomResponseDTO.class)))})
    @Operation(description = "이용자 프로필을 읽어 교사 이용자가 업데이트한 학급의 finedust_factor와 ultrafine_factor를 가져옵니다. 데이터가 없을 경우 두 값은 -1로 반환합니다")
    public ClassroomResponseDTO getClassroom(@RequestParam String schoolCode,
                                             @RequestParam String grade,
                                             @RequestParam String classNum) throws ExecutionException, InterruptedException {
        return classroomService.get(schoolCode, grade, classNum);
    }
}
