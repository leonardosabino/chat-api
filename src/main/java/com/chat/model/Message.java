package com.chat.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message {

    @NotNull
    private String nickName;

    @NotNull
    private String message;

}
