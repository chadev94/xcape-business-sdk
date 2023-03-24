package com.chadev.xcape.admin.controller;

import com.chadev.xcape.core.response.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdminControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public Response<Void> handleException(IllegalArgumentException e) {
        return Response.error(e.getMessage());
    }
}
