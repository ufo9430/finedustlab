package com.finedustlab.controller;

import com.finedustlab.model.api.LocalFinedustResponseDTO;
import com.finedustlab.model.user.TeacherProfile;
import com.finedustlab.model.user.UserInputWrapper;
import com.finedustlab.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Tag(name = "setUserProfile")
    @Operation(description = "이용자 프로필을 firestore에 저장합니다.")
    @PostMapping("/user/set")
    @ResponseBody
    public void setUser(@RequestBody UserInputWrapper user) throws FirebaseAuthException {
        userService.set(user.getUid(), user.getUserProfile());
    }

    @Tag(name = "getUserProfile")
    @Operation(description = "이용자 정보를 통해 이용자 프로필을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이용자 조회 성공", content = @Content(schema = @Schema(implementation = TeacherProfile.class)))})
    @GetMapping("/user/get")
    @ResponseBody
    public Map<String, Object> getUser(@RequestParam String uid) throws ExecutionException, FirebaseAuthException, InterruptedException {
        return userService.get(uid);
    }

    @Tag(name = "findUserEmail")
    @Operation(description = "이용자 정보를 통해 이용자 이메일을 불러옵니다.")
    @GetMapping("/user/findEmail")
    @ResponseBody
    public Map<String, String> getEmail(@RequestParam String name, @RequestParam String schoolCode) throws ExecutionException, InterruptedException{
        return userService.getEmail(name,schoolCode);
    }
}
