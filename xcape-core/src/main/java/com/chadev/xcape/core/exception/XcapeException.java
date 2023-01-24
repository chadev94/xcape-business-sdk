package com.chadev.xcape.core.exception;

import com.chadev.xcape.core.response.ErrorCode;

public class XcapeException extends RuntimeException {
    private ErrorCode errorCode;

    public static class NotExistentMerchantException extends XcapeException{
        public final ErrorCode errorCode = ErrorCode.NOT_EXISTENT_MERCHANT;
    }

}
