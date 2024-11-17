package com.eatpizzaquickly.concertservice.repository;

import com.eatpizzaquickly.concertservice.exception.detail.SeatAlreadyReservedException;

public enum SeatReservationCode {
    SUCCESS(1),
    ALREADY_RESERVED(2);

    private final int code;

    SeatReservationCode(int code) {
        this.code = code;
    }

    public static SeatReservationCode find(String code) {
        int codeValue = Integer.parseInt(code);
        if (codeValue == 1) return SUCCESS;
        if (codeValue == 2) return ALREADY_RESERVED;
        throw new IllegalArgumentException("존재하지 않는 코드입니다. %s".formatted(code));
    }

    public static void checkReservationResult(SeatReservationCode code) {
        if (code == ALREADY_RESERVED) {
            throw new SeatAlreadyReservedException("이미 예약된 좌석입니다.");
        }
    }
}
