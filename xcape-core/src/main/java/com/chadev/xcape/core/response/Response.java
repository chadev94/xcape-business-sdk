package com.chadev.xcape.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {

    private String resultCode;
    private String resultMessage;
    private T result;

    public static <T> Response<T> error(String resultMessage) {
        return new Response<>(ErrorCode.SERVER_ERROR.getCode(), resultMessage, null);
    }

    public static Response<Void> success() {
        return new Response<>("SUCCESS", null, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", null, result);
    }
}
