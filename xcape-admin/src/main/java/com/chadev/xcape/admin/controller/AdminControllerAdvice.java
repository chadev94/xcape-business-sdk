package com.chadev.xcape.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AdminControllerAdvice {

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletRequest request, Exception e) {
        log.error(">>> {}: \"{}\" error: \n", request.getMethod(), request.getServletPath(), e);
    }
}
