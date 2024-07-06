package com.home.it.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {
    @Getter
    private final HttpStatus httpStatus;
    @Getter
    private final String headerMessage;

    public ServiceException(String message, String headerMessage, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.headerMessage = headerMessage;
    }

}
