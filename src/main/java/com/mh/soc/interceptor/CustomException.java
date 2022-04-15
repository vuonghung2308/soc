package com.mh.soc.interceptor;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final String code;
    private final HttpStatus status;

    public CustomException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public CustomException(String code, String message) {
        super(message);
        this.status = HttpStatus.OK;
        this.code = code;
    }
}
