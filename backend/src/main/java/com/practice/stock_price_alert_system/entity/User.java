package com.practice.stock_price_alert_system.entity;

import com.practice.stock_price_alert_system.model.Role;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="users")
@NamedQuery(name = "User.findUserByUsername", query="SELECT u FROM User u WHERE u.username = :username")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
