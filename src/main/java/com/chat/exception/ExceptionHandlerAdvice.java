package com.chat.exception;

import com.chat.model.ApiErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiErrorDTO handle(Exception ex) {
        return new ApiErrorDTO(ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorDTO handle(MethodArgumentNotValidException ex) {
        return new ApiErrorDTO(getMethodArgumentErrors(ex.getBindingResult()),
                HttpStatus.BAD_REQUEST.value());
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
