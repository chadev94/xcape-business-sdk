package com.chadev.xcape.api.controller;

import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class ApiRestControllerAdvice {

    @ExceptionHandler(Exception.class)
    public Response<Void> handleException(Exception e) {
        log.error(String.valueOf(e));
        return Response.error(e.getMessage());
    }
}
