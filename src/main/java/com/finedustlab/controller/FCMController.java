package com.finedustlab.controller;

import com.finedustlab.model.fcm.MessageInputDto;
import com.finedustlab.service.FCMService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notification")
public class FCMController {

    @Autowired
    private FCMService fcmService;

    @PostMapping
    @Operation(description = "클라이언트의 FCM 토큰과 학교 코드를 입력받아 푸시 알림 형태로 알림을 보냅니다.")
    public String sendMessageByToken(@RequestBody MessageInputDto messageDto){
        return fcmService.send(messageDto);
    }
}
