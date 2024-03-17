package com.finedustlab.service.FCM;

import com.finedustlab.domain.repository.UserRepository;
import com.finedustlab.model.FCM.FCMNotificationRequestDto;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public String sendNotificationByToken(FCMNotificationRequestDto requestDto){
        Object user = userRepository.findByID(requestDto.getTargetUserId());

        if(user.isPresent()){

        }
    }

}
