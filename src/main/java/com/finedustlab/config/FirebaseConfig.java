package com.finedustlab.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FirebaseConfig {

    public static final String SERVICE_ACCOUNT_KEY = "serviceAccountKey.json";

    @PostConstruct
    public void init(){
        try{
            ClassPathResource res = new ClassPathResource(SERVICE_ACCOUNT_KEY);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(res.getInputStream()))
                    .setDatabaseUrl("https://finedustlab-default-rtdb.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
