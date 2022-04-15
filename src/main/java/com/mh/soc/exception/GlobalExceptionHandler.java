package com.mh.soc.exception;


import com.mh.soc.interceptor.CustomException;
import com.mh.soc.vo.response.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleException(CustomException exception) {
        ResponseMessage msg = ResponseMessage.get(exception);
        return new ResponseEntity<>(msg, exception.getStatus());
    }
}
