package com.chadev.xcape.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SERVER_ERROR("9000", "서버 에러가 발생했습니다."),
    NOT_EXISTENT_MERCHANT("9001", "존재하지 않는 지점입니다."),
    NOT_EXISTENT_THEME("9002", "존재하지 않는 테마입니다."),
    NOT_EXISTENT_RESERVATION("9003", "존재하지 않는 예약입니다."),
    NOT_EXISTENT_ABILITY("9004", "존재하지 않는 어빌리티입니다."),
    NOT_EXISTENT_BANNER("9005", "존재하지 않는 배너입니다."),
    NOT_EXISTENT_CLOSED_DATE("9006", "존재하지 않는 휴무입니다."),
    NOT_EXISTENT_PRICE("9007", "존재하지 않는 가격정책입니다."),
    NOT_EXISTENT_RESERVATION_HISTORY("9008", "존재하지 않는 예약기록입니다."),
    NOT_EXISTENT_SCHEDULER("9009", "존재하지 않는 스케줄러입니다."),
    ALREADY_RESERVATION("9010", "이미 예약된 예약내역입니다."),
    NOT_EXISTENT_TIMETABLE("9011", "존재하지 않는 타임테이블입니다."),
    OVERFLOW_RESERVATION("9012", "예약 가능한 인원을 초과했습니다."),
    NOT_EXISTENT_ROOM_TYPE("9012", "존재하지 않는 룸 타입입니다."),

    // 인증
    AUTHENTICATION_INVALID_PHONE_NUMBER("9100", "예약자의 연락처와 일치하지 않는 연락처입니다."),
    AUTHENTICATION_TIME_OUT("9101", "인증 시간이 초과하였습니다."),
    AUTHENTICATION_INVALID_NUMBER("9102", "인증번호가 일치하지 않습니다.");


    private final String code;
    private final String message;
}
