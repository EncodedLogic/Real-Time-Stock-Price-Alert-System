package com.practice.stock_price_alert_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;
    private String eventName;
    private LocalDateTime eventDate;
    private boolean booked;
}

