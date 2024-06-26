package com.finedustlab.service;

import com.finedustlab.model.api.LocalFinedustResponseDTO;
import com.finedustlab.model.fcm.FCMMessage;
import com.finedustlab.model.fcm.MessageInputDto;
import com.finedustlab.service.api.FinedustLocalService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class FCMService {
    private final FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

    @Autowired
    private FinedustLocalService service;

    public Map<String, String> getAccessToken() {
        Map<String, String> result = new HashMap<>();
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource("serviceAccountKey.json").getInputStream())
                    .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();

            String tokenValue = googleCredentials.getAccessToken().getTokenValue();

            result.put("status","complete");
            result.put("tokenValue",tokenValue);
            return result;
        }catch (IOException e){
            e.printStackTrace();
            result.put("status","error");
            result.put("tokenValue","-");
            return result;
        }
    }


    public String send(MessageInputDto messageInputDto){
        String recipientToken = messageInputDto.getRecipientToken();
        String schoolCode = messageInputDto.getSchoolCode();

        FCMMessage message = new FCMMessage();
        message.setRecipientToken(recipientToken);
        message.setTitle("미세먼지 알림");

        try{
            LocalFinedustResponseDTO finedustStatus = service.getFinedustStatus(schoolCode);
            String status = finedustStatus.getFine_status();
            switch(status) {
                case "good":
                    message.setBody("현재 해당 지역 미세먼지 농도는 좋음 입니다.");
                case "fine":
                    message.setBody("현재 해당 지역 미세먼지 농도는 보통 입니다.");
                case "bad":
                    message.setBody("현재 해당 지역 미세먼지 농도는 나쁨 입니다.");
            }
            return sendMessageByToken(message);
        } catch (Exception e){
            e.printStackTrace();
            return "메세지 전송 중 오류가 발생했습니다 getFinedustStatus";
        }
    }
    public String sendMessageByToken(FCMMessage messageDto) {
        Notification notification = Notification.builder()
                .setTitle(messageDto.getTitle())
                .setBody(messageDto.getBody())
                .build();
        com.google.firebase.messaging.Message message = com.google.firebase.messaging.Message
                .builder()
                .setToken(messageDto.getRecipientToken())
                .setNotification(notification)
                .build();

        try{
            firebaseMessaging.send(message);
            return "messageDto.getRecipientToken() = " + messageDto.getRecipientToken();
        }catch (FirebaseMessagingException e){
            e.printStackTrace();
            return "메세지 전송 중 오류가 발생했습니다 sendMessageByToken";
        }
    }
}
