package com.practice.stock_price_alert_system.controller;

import com.practice.stock_price_alert_system.entity.User;
import com.practice.stock_price_alert_system.exception.GlobalException;
import com.practice.stock_price_alert_system.model.Role;
import com.practice.stock_price_alert_system.security.UserLoadedFromDbBySecurity;
import com.practice.stock_price_alert_system.service.SecurityService;
import com.practice.stock_price_alert_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SecurityService securityService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(SecurityService securityService, UserService userService,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder){
        this.securityService = securityService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials){
        try{
            String username = credentials.get("username");
            String password = credentials.get("password");

            Authentication authenticatedDetails = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,password)
            );

            UserLoadedFromDbBySecurity userDetails = (UserLoadedFromDbBySecurity) authenticatedDetails.getPrincipal();
            String jwToken = securityService.jwtGenerator(userDetails);
            String role = userDetails.getAuthorities().stream().findFirst()
                    .map(GrantedAuthority::getAuthority).orElseThrow(() -> new GlobalException("No role found for this user"));

            Map<String, Object> response = new HashMap<>();
            response.put("jwToken",jwToken);
            response.put("username",securityService.usernameExtractor(jwToken));
            response.put("role",role);
            System.out.println("Generated Token : "+jwToken);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch(Exception e){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or Password");}
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> newUserData){
        try{
            String username = newUserData.get("username");
            String password = newUserData.get("password");
            String role = newUserData.get("role");

            if (userService.doesUserExist(username)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken!");
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRole(Role.valueOf(role.toUpperCase()));

            userService.registerNewUser(newUser).orElseThrow(() -> new GlobalException("User not registered successfully"));

            return ResponseEntity.status(HttpStatus.OK).body("New User Successfully Registered");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Role or Input");
        }
    }

}
