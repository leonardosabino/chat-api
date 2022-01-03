package com.webhook.chat.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {

    private String nickName;
    private String message;
    private LocalDateTime createdAt;

}
