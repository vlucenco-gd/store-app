package com.vlucenco.springframework.storeapp.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class NotEnoughStockException extends ResponseStatusException {
    public NotEnoughStockException() {
        super(CONFLICT, "Not enough stock available");
    }
}
