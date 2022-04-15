package com.mh.soc.vo.response;

import com.mh.soc.interceptor.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseMessage {
    private final String code;
    private final String msg;

    public static ResponseMessage get(CustomException e) {
        return new ResponseMessage(e.getCode(), e.getMessage());
    }
}