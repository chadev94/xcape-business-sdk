package com.chadev.xcape.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INVALID_PERMISSION("fail", "Permission is invalid"),
    NOT_EXISTENT_DATA("fail", "Not existent"),
    NOT_EXISTENT_MERCHANT("fail", "존재하지 않는 지점입니다.")
    ;

    private final String errorCode;
    private final String errorMessage;
}
