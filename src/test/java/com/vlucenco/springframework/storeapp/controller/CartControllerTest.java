package com.vlucenco.springframework.storeapp.controller;

import com.vlucenco.springframework.storeapp.model.dto.CartItemRequest;
import com.vlucenco.springframework.storeapp.model.dto.CartResponse;
import com.vlucenco.springframework.storeapp.model.entity.Cart;
import com.vlucenco.springframework.storeapp.model.entity.CartItem;
import com.vlucenco.springframework.storeapp.model.entity.Product;
import com.vlucenco.springframework.storeapp.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CartControllerTest {

    public static final String CART_PATH = "/api/v1/cart";

    private WebTestClient webTestClient;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private Product product;
    private CartItem cartItem;
    private Cart cart;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(cartController).build();

        product = Product.builder()
                .id("1")
                .name("Sample Product")
                .price(BigDecimal.TEN)
                .availableQuantity(10)
                .build();

        cartItem = CartItem.builder()
                .product(product)
                .quantity(2)
                .build();

        cart = Cart.builder()
                .sessionId("test-session")
                .items(Collections.singletonMap("1", cartItem))
                .build();
    }

    @Test
    void addProductToCart_ShouldReturnCartResponse() {
        CartItemRequest request = new CartItemRequest("1", 2);

        when(cartService.addProductToCart(any(), any(), anyInt())).thenReturn(Mono.just(cart));

        webTestClient.post()
                .uri(CART_PATH + "/test-session/add")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CartResponse.class);
    }

    @Test
    void getCart_ShouldReturnCartResponse() {
        when(cartService.getCart("test-session")).thenReturn(Mono.just(cart));

        webTestClient.get()
                .uri(CART_PATH + "/test-session")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CartResponse.class);
    }
}
