package com.practice.stock_price_alert_system.controller;

import com.practice.stock_price_alert_system.entity.User;
import com.practice.stock_price_alert_system.exception.GlobalException;
import com.practice.stock_price_alert_system.model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AlertController {

    class Main1 {
        public static void main(String[] args) {
            LinkedList<String> animals= new LinkedList<>();

            // Add elements in LinkedList
            animals.add("Dog");
            animals.add("Horse");
            animals.add("Cat");

            // Create an object of ListIterator
            ListIterator<String> listIterate = animals.listIterator();
            System.out.print("LinkedList: ");

            while(listIterate.hasNext()) {
                System.out.print(listIterate.next());
                System.out.print(", ");
            }

            // Iterate backward
            System.out.print("\nReverse LinkedList: ");

            while(listIterate.hasPrevious()) {
                System.out.print(listIterate.previous());
                System.out.print(", ");
            }
        }
    }


    class Main2 {
        public static void main(String[] args) {
            LinkedList<String> animals= new LinkedList<>();

            // Add elements in LinkedList
            animals.add("Dog");
            animals.add("Horse");
            animals.add("Cat");

            // Creating an object of Iterator
            Iterator<String> iterate = animals.iterator();
            System.out.print("LinkedList: ");

            while(iterate.hasNext()) {
                System.out.print(iterate.next());
                System.out.print(", ");
            }
        }
    }
}


class Main3 {
    public static void main(String[] args) {
        LinkedList<String> languages = new LinkedList<>();

        // add elements in the LinkedList
        languages.add("Python");
        languages.add("Java");
        languages.add("JavaScript");
        System.out.println("LinkedList: " + languages);

        // get the element from the LinkedList
        String str = languages.get(1);
        System.out.print("Element at index 1: " + str);
    }
}

    @GetMapping("/")
    public ResponseEntity<List<Map<String, Object>>> getTopGainersFromYahoo() {
        try {
            List<Map<String, Object>> gainers = stockService.getTopGainersDetails();
            if (gainers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
            return ResponseEntity.ok(gainers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
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
