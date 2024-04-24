package com.finedustlab.controller;

import com.finedustlab.model.user.UserAccount;
import com.finedustlab.model.user.UserInputWrapper;
import com.finedustlab.model.user.StudentProfile;
import com.finedustlab.model.user.UserProfile;
import com.finedustlab.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        userService.set(user.getEmail(), user.getUserProfile());
    }

    @Tag(name = "setUserProfile")
    @Operation(description = "이용자 정보를 통해 이용자 프로필을 불러옵니다.")
    @PostMapping("/user/get")
    @ResponseBody
    public Map<String, Object> getUser(@RequestParam String email) throws ExecutionException, FirebaseAuthException, InterruptedException {
        return userService.get(email);
    }
}
