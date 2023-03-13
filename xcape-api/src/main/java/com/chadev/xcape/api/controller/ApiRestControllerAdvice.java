package com.chadev.xcape.api.controller;

import com.chadev.xcape.core.response.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiRestControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public Response<Void> handleException(IllegalArgumentException e) {
        return Response.error(e.getMessage());
    }
}
