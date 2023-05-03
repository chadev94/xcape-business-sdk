package com.chadev.xcape.api.controller;

import com.chadev.xcape.core.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiRestControllerAdvice {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletRequest request) {
        log.error(">>> {} error: ", request.getServletPath(), e);
    }
}
