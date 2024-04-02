package com.finedustlab.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.model.fcm.FCMMessageDto;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class FCMSerivce {
    private final ObjectMapper objectMapper;
    @Autowired
    public FCMSerivce(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("serviceAccountKey.json").getInputStream())
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return  googleCredentials.getAccessToken().getTokenValue();
    }
//
//    public String makeMessage(
//            String targetToken, String title, String body, String name, String description
//    ){
//        FCMMessageDto fcmMessage = new FCMMessageDto();
//    }
}
