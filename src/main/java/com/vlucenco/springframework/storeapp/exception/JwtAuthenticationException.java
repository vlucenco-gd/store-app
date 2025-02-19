package com.vlucenco.springframework.storeapp.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class JwtAuthenticationException extends ResponseStatusException {
    public JwtAuthenticationException(String message) {
        super(UNAUTHORIZED, message);
    }
}
