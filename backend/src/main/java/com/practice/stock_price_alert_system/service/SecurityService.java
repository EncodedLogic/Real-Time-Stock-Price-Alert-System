package com.practice.stock_price_alert_system.service;

import com.practice.stock_price_alert_system.entity.User;
import com.practice.stock_price_alert_system.repository.UserRepository;
import com.practice.stock_price_alert_system.security.JwtUtil;
import com.practice.stock_price_alert_system.security.UserLoadedFromDbBySecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public SecurityService(UserRepository userRepository, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserLoadedFromDbBySecurity loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found : "+username));
        return new UserLoadedFromDbBySecurity(user);
    }

    public String jwtGenerator(UserLoadedFromDbBySecurity userDetails){
        return jwtUtil.generateJwt(userDetails);
    }

    public String usernameExtractor(String jwToken){
        return jwtUtil.extractUsernameFromJwt(jwToken);
    }

}
