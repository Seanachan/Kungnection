package org.kungnection.controller;

import org.kungnection.model.User;
import org.kungnection.service.UserService;
import org.kungnection.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations (login and registration).
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new user account.
     */
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * Authenticates a user and returns a JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User authenticatedUser = userService.login(user.getEmail(), user.getPassword());
        if (authenticatedUser != null) {
            String token = jwtUtil.generateToken(authenticatedUser.getId());
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.status(401).body("Login failed.");
        }
    }
}
