package com.github.sasachichito.agileplanning.port.adapter.exception;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ResourceNotFoundException extends RuntimeException {
    @Getter
    private String message;

    public ResourceNotFoundException(String message) {
        this.message = message;
    }
}
