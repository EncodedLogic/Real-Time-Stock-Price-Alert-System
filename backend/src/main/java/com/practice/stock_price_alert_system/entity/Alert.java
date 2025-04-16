package com.practice.stock_price_alert_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Alert {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String username;

        @Column(unique = true)
        private String email;

        private String password;

        private String role;
    }

