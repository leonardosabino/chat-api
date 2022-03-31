package com.chat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ApiError implements Serializable {

    private static final long serialVersionUID = 5894637220972145408L;

    private String code;

    private String message;

    public ApiError(String message, int code) {
        this.code = String.valueOf(code);
        this.message = message;
    }

}
