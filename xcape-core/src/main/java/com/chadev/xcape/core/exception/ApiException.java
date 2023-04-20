package com.chadev.xcape.core.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends RuntimeException{
    public static ApiException API_EXCEPTION(String message) {
        return new ApiException(9001, message);
    };

    private int statusCode;
    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
