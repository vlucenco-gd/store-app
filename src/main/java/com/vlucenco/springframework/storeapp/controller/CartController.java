package com.vlucenco.springframework.storeapp.controller;

import com.vlucenco.springframework.storeapp.model.dto.CartItemRequest;
import com.vlucenco.springframework.storeapp.model.dto.CartResponse;
import com.vlucenco.springframework.storeapp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{sessionId}/add")
    public Mono<ResponseEntity<CartResponse>> addProductToCart(@PathVariable String sessionId,
                                                               @RequestBody CartItemRequest request) {
        return cartService.addProductToCart(sessionId, request.getProductId(), request.getQuantity())
                .map(cart -> ResponseEntity.ok(CartResponse.from(cart)));
    }

    @GetMapping("/{sessionId}")
    public Mono<ResponseEntity<CartResponse>> getCart(@PathVariable String sessionId) {
        return cartService.getCart(sessionId)
                .map(cart -> ResponseEntity.ok(CartResponse.from(cart)));
    }

    @DeleteMapping("/{sessionId}/remove/{productId}")
    public Mono<ResponseEntity<CartResponse>> removeProductFromCart(@PathVariable String sessionId,
                                                            @PathVariable String productId) {
        return cartService.removeProductFromCart(sessionId, productId)
                .map(cart -> ResponseEntity.ok(CartResponse.from(cart)));
    }

    @PutMapping("/{sessionId}/update")
    public Mono<ResponseEntity<CartResponse>> updateCartItem(@PathVariable String sessionId,
                                                             @RequestBody CartItemRequest request) {
        return cartService.updateCartItem(sessionId, request.getProductId(), request.getQuantity())
                .map(cart -> ResponseEntity.ok(CartResponse.from(cart)));
    }
}
