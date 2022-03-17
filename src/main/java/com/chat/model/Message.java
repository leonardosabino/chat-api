package com.chat.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class Message {

    @NotNull
    private String nickName;
    @NotNull
    private String message;
    private LocalDateTime createdAt;

}
