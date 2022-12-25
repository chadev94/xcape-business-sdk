package com.chadev.xcape.core.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INVALID_PERMISSION("Permission is invalid"),
    NOT_EXISTENT_DATA("Not existent")
    ;

    private final String errorCode;
}
