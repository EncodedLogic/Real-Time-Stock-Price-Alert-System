package com.practice.stock_price_alert_system.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private String exceptionMessage;
    private LocalDateTime timeStamp;
}
