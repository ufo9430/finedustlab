package com.finedustlab.model.fcm;

import lombok.Data;

@Data
public class MessageInputDto {
    private String recipientToken;
    private String schoolCode;
}