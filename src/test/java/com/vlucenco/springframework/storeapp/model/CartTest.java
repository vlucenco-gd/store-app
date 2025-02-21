package com.vlucenco.springframework.storeapp.model;

import com.vlucenco.springframework.storeapp.model.entity.Cart;
import com.vlucenco.springframework.storeapp.model.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CartTest {
    private Cart cart;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        cart = Cart.builder().userId("session-123").build();
        product1 = Product.builder().id("1").name("Product A")
                .price(BigDecimal.valueOf(10.50)).availableQuantity(10).build();
        product2 = Product.builder().id("2").name("Product B")
                .price(BigDecimal.valueOf(20.00)).availableQuantity(5).build();
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
