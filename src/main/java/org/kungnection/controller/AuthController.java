package org.kungnection.controller;

import org.kungnection.model.User;
import org.kungnection.service.UserService;
import org.kungnection.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User u = userService.login(user.getEmail(), user.getPassword()); // ← 注意這裡是 getEmail()
        if (u != null) {
            String token = jwtUtil.generateToken(u.getId());
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.status(401).body("Login failed.");
        }
    }
}