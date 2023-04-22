package com.chadev.xcape.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class XcapeException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public XcapeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.message = errorCode.getMessage();
        this.errorCode = errorCode;
    }

    public static XcapeException NOT_EXISTENT_MERCHANT() {return new XcapeException(ErrorCode.NOT_EXISTENT_MERCHANT);}

    public static XcapeException NOT_EXISTENT_THEME() {return new XcapeException(ErrorCode.NOT_EXISTENT_THEME);}

    public static XcapeException NOT_EXISTENT_RESERVATION() {return new XcapeException(ErrorCode.NOT_EXISTENT_RESERVATION);}
}