package com.finedustlab.service;

import com.finedustlab.model.fcm.FCMMessage;
import com.finedustlab.model.fcm.MessageInputDto;
import com.finedustlab.service.api.FinedustLocalService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Notification;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class FCMService {
    private final FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

    @Autowired
    private FinedustLocalService service;

    public String getAccessToken() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("serviceAccountKey.json").getInputStream())
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();

    }
    public String send(MessageInputDto messageInputDto){
        String recipientToken = messageInputDto.getRecipientToken();
        String schoolCode = messageInputDto.getSchoolCode();

        FCMMessage message = new FCMMessage();
        message.setRecipientToken(recipientToken);
        message.setTitle("미세먼지 알림");

        try{
            Map<String, String> finedustStatus = service.getFinedustStatus(schoolCode);
            String status = finedustStatus.get("status");
            if(status.equals("good")){
                message.setBody("현재 해당 지역 미세먼지 농도는 좋음 입니다.");
            }else{
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
