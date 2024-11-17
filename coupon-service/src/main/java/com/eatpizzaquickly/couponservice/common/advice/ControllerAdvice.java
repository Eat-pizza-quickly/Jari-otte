package com.eatpizzaquickly.couponservice.common.advice;

import com.eatpizzaquickly.couponservice.common.exception.*;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(NotFoundException.class) // 404 Not Found
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class) // 400 Bad Request
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
    @ExceptionHandler(ForbiddenException.class) // 403 Forbidden
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage()));
    }


    @ExceptionHandler(UnauthorizedException.class) // 401 Unauthorized
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
    }

}