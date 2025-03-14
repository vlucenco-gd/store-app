package com.vlucenco.springframework.storeapp.controller;

import com.vlucenco.springframework.storeapp.model.dto.CartItemRequest;
import com.vlucenco.springframework.storeapp.model.dto.CartResponse;
import com.vlucenco.springframework.storeapp.security.JwtUtil;
import com.vlucenco.springframework.storeapp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    @PostMapping("/add")
    public Mono<ResponseEntity<CartResponse>> addProductToCart(@AuthenticationPrincipal UsernamePasswordAuthenticationToken auth,
                                                               @RequestBody @Validated CartItemRequest request) {
        return cartService.addProductToCart(jwtUtil.extractUserId(auth), request.getProductId(), request.getQuantity())
                .map(cart -> ResponseEntity.ok(CartResponse.from(cart)));
    }

    @GetMapping()
    public Mono<ResponseEntity<CartResponse>> getCart(@AuthenticationPrincipal UsernamePasswordAuthenticationToken auth) {
        return cartService.getCart(jwtUtil.extractUserId(auth))
                .map(cart -> ResponseEntity.ok(CartResponse.from(cart)));
    }

    @DeleteMapping("/remove/{productId}")
    public Mono<ResponseEntity<CartResponse>> removeProductFromCart(@AuthenticationPrincipal UsernamePasswordAuthenticationToken auth,
                                                                    @PathVariable String productId) {
        return cartService.removeProductFromCart(jwtUtil.extractUserId(auth), productId)
                .map(cart -> ResponseEntity.ok(CartResponse.from(cart)));
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<CartResponse>> updateCartItem(@AuthenticationPrincipal UsernamePasswordAuthenticationToken auth,
                                                             @RequestBody @Validated CartItemRequest request) {
        return cartService.updateCartItem(jwtUtil.extractUserId(auth), request.getProductId(), request.getQuantity())
                .map(cart -> ResponseEntity.ok(CartResponse.from(cart)));
    }

    @PostMapping("/checkout")
    public Mono<ResponseEntity<String>> checkoutCart(@AuthenticationPrincipal User user) {
        String userId = user.getUsername();
        return cartService.checkoutCart(userId)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }
}
