package com.practice.stock_price_alert_system.service;

import com.practice.stock_price_alert_system.entity.User;
import com.practice.stock_price_alert_system.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
    public class UserServiceImpl {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private JwtUtil jwtUtil;

        @Override
        public User register(UserDTO userDTO) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("Email already taken!");
            }

            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setRole("ROLE_USER");

            return userRepository.save(user);
        }

        @Override
        public String login(LoginRequest request) {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid credentials");
            }

            return jwtUtil.generateToken(user);
        }

        @Override
        public User getProfile(String email) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }
    }

}
