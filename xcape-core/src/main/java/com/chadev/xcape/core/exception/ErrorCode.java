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
    ;


    private final String code;
    private final String message;
}
