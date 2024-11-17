package com.eatpizzaquickly.concertservice.exception;

import com.eatpizzaquickly.concertservice.dto.response.ErrorResponse;
import com.eatpizzaquickly.concertservice.exception.detail.RequestLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(RequestLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRequestLimitExceededException(RequestLimitExceededException e) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ErrorResponse(e.getMessage()));
    }
}
