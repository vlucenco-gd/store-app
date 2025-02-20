package com.vlucenco.springframework.storeapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CartTest {
    private Cart cart;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        cart = new Cart("session-123", new HashMap<>());
        product1 = new Product("1", "Product A", new BigDecimal("10.50"), 10);
        product2 = new Product("2", "Product B", new BigDecimal("20.00"), 5);
    }

    @Test
    void addProduct() {
        cart.addProduct(product1, 2);
        assertEquals(2, cart.getItems().get("1").getQuantity());
    }

    @Test
    void updateProduct() {
        cart.addProduct(product1, 2);
        cart.updateProduct("1", 5);
        assertEquals(5, cart.getItems().get("1").getQuantity());
    }

    @Test
    void removeProduct() {
        cart.addProduct(product1, 2);
        cart.removeProduct("1");
        assertFalse(cart.getItems().containsKey("1"));
    }

    @Test
    void calculateSubtotal() {
        cart.addProduct(product1, 2);
        cart.addProduct(product2, 1);
        BigDecimal expectedTotal = BigDecimal.valueOf((10.50 * 2) + (20.00 * 1));
        assertEquals(0, expectedTotal.compareTo(cart.calculateSubtotal()));
    }

    @Test
    void emptyCartSubtotal() {
        assertEquals(BigDecimal.ZERO, cart.calculateSubtotal());
    }
}
