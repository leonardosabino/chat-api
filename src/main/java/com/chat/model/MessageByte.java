package com.chat.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageByte {

    private byte[] nickName;

    private byte[] message;

    private LocalDateTime createdAt;

}
