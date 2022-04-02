package com.chat.exception;

import com.chat.model.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value = CryptoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiError handle(CryptoException ex) {
        ex.printStackTrace();
        return new ApiError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handle(MethodArgumentNotValidException ex) {
        return new ApiError(getMethodArgumentErrors(ex.getBindingResult()), HttpStatus.BAD_REQUEST.value());
    }

    private String getMethodArgumentErrors(BindingResult bindResults) {
        StringBuilder res = new StringBuilder();
        for (ObjectError e : bindResults.getAllErrors()) {
            String campo = ((FieldError) e).getField();
            res.append(campo).append(" : ").append(e.getDefaultMessage()).append(" ; ");
        }
        return res.toString();
    }

}
