package com.chadev.xcape.core.response;

import com.chadev.xcape.core.exception.ErrorCode;
import com.chadev.xcape.core.exception.XcapeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {

    private String resultCode;
    private String resultMessage;
    private T result;

    public static <T> Response<T> error(ErrorCode e) {
        return new Response<>(e.getCode(), e.getMessage(), null);
    }

    public static <T> Response<T> error(String resultMessage) {
        return new Response<>(ErrorCode.SERVER_ERROR.getCode(), resultMessage, null);
    }

    public static <T> Response<T> error(XcapeException e) {
        return new Response<>(e.getErrorCode(), e.getMessage(), null);
    }

    public static Response<Void> success() {
        return new Response<>("SUCCESS", null, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", null, result);
    }
}
