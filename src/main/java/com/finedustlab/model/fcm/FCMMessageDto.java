package com.finedustlab.model.fcm;


import lombok.*;


@Builder
@Getter
@AllArgsConstructor
public class FCMMessageDto {
    private boolean validateOnly;
    private Message message;

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Message{
        private Notification notification;
        private String token;
        private Data data;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    public static class Notification{
        private String title;
        private String body;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Data{
        private String name;
        private String description;
    }
}
