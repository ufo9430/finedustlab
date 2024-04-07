package com.finedustlab.controller;

import com.finedustlab.model.fcm.MessageInputDto;
import com.finedustlab.service.FCMService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/notification")
public class FCMController {

    @Autowired
    private FCMService fcmService;

    @GetMapping("/token")
    @Operation(description = "FCM 토큰을 생성해 반환합니다. 토큰은 앱이 Instance ID 를 삭제할때, 앱이 재설치될 때, 유저가 앱 데이터를 초기화할 때 갱신됩니다.")
    public String getFCMAccessToken() {
        return fcmService.getAccessToken();
    }


    @PostMapping
    @Operation(description = "클라이언트의 FCM 토큰과 학교 코드를 입력받아 푸시 알림 형태로 알림을 보냅니다.")
    public String sendMessageByToken(@RequestBody MessageInputDto messageDto){
        return fcmService.send(messageDto);
    }
}
