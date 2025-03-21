package com.practice.stock_price_alert_system.exception;

import com.practice.stock_price_alert_system.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlerZone {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerZone.class);

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(GlobalException globalException){
        logger.error("An exception occurred : {}",globalException.getMessage());
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, globalException.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
