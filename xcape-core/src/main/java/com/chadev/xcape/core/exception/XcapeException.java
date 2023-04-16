package com.chadev.xcape.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class XcapeException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public static XcapeException NOT_EXISTENT_MERCHANT() {
        return new XcapeException(
                ErrorCode.NOT_EXISTENT_MERCHANT,
                ErrorCode.NOT_EXISTENT_MERCHANT.getMessage()
        );
    }

    public static XcapeException NOT_EXISTENT_THEME() {
        return new XcapeException(
                ErrorCode.NOT_EXISTENT_THEME,
                ErrorCode.NOT_EXISTENT_THEME.getMessage()
        );
    }

    public static XcapeException NOT_EXISTENT_RESERVATION() {
        return new XcapeException(
                ErrorCode.NOT_EXISTENT_RESERVATION,
                ErrorCode.NOT_EXISTENT_RESERVATION.getMessage()
        );
    }
}
