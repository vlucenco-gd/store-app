package com.vlucenco.springframework.storeapp.controller;

import com.vlucenco.springframework.storeapp.exception.NotEnoughStockException;
import com.vlucenco.springframework.storeapp.model.dto.CartItemRequest;
import com.vlucenco.springframework.storeapp.model.dto.CartResponse;
import com.vlucenco.springframework.storeapp.model.entity.Cart;
import com.vlucenco.springframework.storeapp.model.entity.CartItem;
import com.vlucenco.springframework.storeapp.model.entity.Product;
import com.vlucenco.springframework.storeapp.security.JwtUtil;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CartControllerTest {

    public static final String CART_PATH = "/api/v1/cart";
    public static final String USERNAME = "test-user";

    private WebTestClient webTestClient;

    @Mock
    private CartService cartService;
    @Mock
    private JwtUtil jwtUtil;

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
                .userId(USERNAME)
                .items(Collections.singletonMap("1", cartItem))
                .build();

        when(jwtUtil.extractUserId(any())).thenReturn(USERNAME);
    }

    @Test
    void addProductToCart_ShouldReturnCartResponse() {
        CartItemRequest request = new CartItemRequest("1", 2);

        when(cartService.addProductToCart(any(), any(), anyInt())).thenReturn(Mono.just(cart));

        webTestClient.post()
                .uri(CART_PATH + "/add")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CartResponse.class)
                .value(cartResponse -> assertEquals(USERNAME, cartResponse.getUserId()));
    }

    @Test
    void getCart_ShouldReturnCartResponse() {
        when(cartService.getCart(USERNAME)).thenReturn(Mono.just(cart));

        webTestClient.get()
                .uri(CART_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CartResponse.class);
    }

    @Test
    void updateCartItem_ShouldModifyCartAndReturnUpdatedResponse() {
        CartItemRequest request = new CartItemRequest("1", 5);
        when(cartService.updateCartItem(any(), any(), anyInt())).thenReturn(Mono.just(cart));

        webTestClient.put()
                .uri(CART_PATH + "/update")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CartResponse.class);
    }

    @Test
    void removeProductFromCart_ShouldReturnUpdatedCart() {
        when(cartService.removeProductFromCart(any(), any())).thenReturn(Mono.just(cart));

        webTestClient.delete()
                .uri(CART_PATH + "/remove/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CartResponse.class);
    }

    @Test
    void addProductToCart_ShouldReturnError_WhenStockIsInsufficient() {
        CartItemRequest request = new CartItemRequest("1", 20);
        when(cartService.addProductToCart(any(), any(), anyInt()))
                .thenReturn(Mono.error(new NotEnoughStockException()));

        webTestClient.post()
                .uri(CART_PATH + "/add")
                .bodyValue(request)
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
