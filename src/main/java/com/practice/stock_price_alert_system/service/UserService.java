package com.practice.stock_price_alert_system.service;

import com.practice.stock_price_alert_system.entity.User;
import com.practice.stock_price_alert_system.exception.GlobalException;
import com.practice.stock_price_alert_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> registerNewUser(User user){
        try {
            return Optional.of(userRepository.save(user));
        }catch(GlobalException e){
            throw new GlobalException("User not registered successfully");
        }
    }

    public boolean doesUserExist(String username) {
        return userRepository.existsByUsername(username);
    }

}
