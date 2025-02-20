package com.vlucenco.springframework.storeapp.service;

import com.vlucenco.springframework.storeapp.model.entity.Cart;
import com.vlucenco.springframework.storeapp.model.entity.Product;
import com.vlucenco.springframework.storeapp.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    public Mono<Cart> addProductToCart(String sessionId, String productId, int quantity) {
        return productService.getProduct(productId)
                .flatMap(product -> validateStock(product, quantity))
                .flatMap(product -> getOrCreateCart(sessionId)
                        .flatMap(cart -> addProductAndSave(cart, product, quantity)));
    }

    public Mono<Cart> updateCartItem(String sessionId, String productId, int quantity) {
        return productService.getProduct(productId)
                .flatMap(product -> validateStock(product, quantity))
                .flatMap(product -> getOrCreateCart(sessionId))
                .flatMap(cart -> updateCartAndSave(cart, productId, quantity))
                .flatMap(this::deleteCartIfEmpty);
    }

    public Mono<Cart> removeProductFromCart(String sessionId, String productId) {
        return getOrCreateCart(sessionId)
                .flatMap(cart -> removeProductAndSave(cart, productId))
                .flatMap(this::deleteCartIfEmpty);
    }

    public Mono<BigDecimal> getCartSubtotal(String sessionId) {
        return getOrCreateCart(sessionId).map(Cart::calculateSubtotal);
    }

    public Mono<Cart> getCart(String sessionId) {
        return getOrCreateCart(sessionId);
    }

    private Mono<Cart> getOrCreateCart(String sessionId) {
        return cartRepository.findById(sessionId)
                .defaultIfEmpty(Cart.builder().sessionId(sessionId).build());
    }

    private Mono<Product> validateStock(Product product, int quantity) {
        return Mono.just(product)
                .filter(p -> p.getAvailableQuantity() >= quantity)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Not enough stock available")));
    }

    private Mono<Cart> deleteCartIfEmpty(Cart cart) {
        return cart.getItems().isEmpty()
                ? cartRepository.deleteById(cart.getSessionId()).then(Mono.empty())
                : Mono.just(cart);
    }

    private Mono<Cart> addProductAndSave(Cart cart, Product product, int quantity) {
        cart.addProduct(product, quantity);
        return cartRepository.save(cart);
    }

    private Mono<Cart> updateCartAndSave(Cart cart, String productId, int quantity) {
        cart.updateProduct(productId, quantity);
        return cartRepository.save(cart);
    }

    private Mono<Cart> removeProductAndSave(Cart cart, String productId) {
        cart.removeProduct(productId);
        return cartRepository.save(cart);
    }
}
