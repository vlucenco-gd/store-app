package com.vlucenco.springframework.storeapp.controller;

import com.vlucenco.springframework.storeapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/auth")
public class UserAuthController {
    private final UserService userService;

    public UserAuthController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public Mono<Void> register(@RequestParam String email, @RequestParam String password) {
        return userService.registerUser(email, password).then();
    }

    @PostMapping("/login")
    public Mono<String> login(@RequestParam String email, @RequestParam String password) {
        return userService.authenticate(email, password)
                .map(user -> "Login successful")
                .defaultIfEmpty("Invalid credentials");
    }
}
