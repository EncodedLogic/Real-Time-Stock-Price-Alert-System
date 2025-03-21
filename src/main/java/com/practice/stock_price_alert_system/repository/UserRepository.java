package com.practice.stock_price_alert_system.repository;

import com.practice.stock_price_alert_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findUserByUsername(@Param("username") String username);
    public boolean existsByUsername(String username);
}
