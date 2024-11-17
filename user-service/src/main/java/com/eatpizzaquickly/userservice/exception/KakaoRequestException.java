package com.eatpizzaquickly.userservice.exception;

import com.eatpizzaquickly.userservice.common.exception.InternalServerException;

public class KakaoRequestException extends InternalServerException {
    public KakaoRequestException(String message) {
        super(message);
    }
}
