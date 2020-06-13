package com.github.sasachichito.agileplanning.port.adapter.exception.handler;

import com.github.sasachichito.agileplanning.port.adapter.exception.ResourceNotFoundException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handle(ResourceNotFoundException e) {
        return ErrorResponse.builder()
                .code("001")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handle(IllegalArgumentException e) {
        return ErrorResponse.builder()
                .code("002")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handle(Exception e) {
        return ErrorResponse.builder()
                .code("000")
                .message(e.getMessage())
                .build();
    }

    @Builder
    @Getter
    public static class ErrorResponse {
        private String code;
        private String message;
    }
}
