package com.chadev.xcape.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_EXISTENT_MERCHANT("존재하지 않는 지점입니다."),
    NOT_EXISTENT_THEME("존재하지 않는 테마입니다."),
    NOT_EXISTENT_RESERVATION("존재하지 않는 예약입니다."),
    NOT_EXISTENT_ABILITY("존재하지 않는 어빌리티입니다."),
    NOT_EXISTENT_BANNER("존재하지 않는 배너입니다."),
    NOT_EXISTENT_CLOSED_DATE("존재하지 않는 휴무입니다."),
    NOT_EXISTENT_PRICE("존재하지 않는 가격정책입니다."),
    NOT_EXISTENT_RESERVATION_HISTORY("존재하지 않는 예약기록입니다."),
    NOT_EXISTENT_SCHEDULER("존재하지 않는 스케줄러입니다."),
    ;


    private final String message;
}
