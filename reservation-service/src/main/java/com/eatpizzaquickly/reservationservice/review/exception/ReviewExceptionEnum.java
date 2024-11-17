package com.eatpizzaquickly.reservationservice.review.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewExceptionEnum {
    REVIEW_UPDATE_ERROR("리뷰 수정 권한이 없습니다."),
    REVIEW_DELETE_ERROR("리뷰 삭제 권한이 없습니다.");

    private final String message;
}
