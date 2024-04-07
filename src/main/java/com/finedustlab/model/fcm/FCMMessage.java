package com.finedustlab.model.fcm;

import lombok.Data;

@Data
public class FCMMessage {
    private String title;
    private String recipientToken;
    private String body;
}
