package com.vlucenco.springframework.storeapp.service;

import com.vlucenco.springframework.storeapp.exception.NotEnoughStockException;
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

    public Mono<Cart> addProductToCart(String userId, String productId, int quantity) {
        return productService.getProduct(productId)
                .flatMap(product -> validateStock(product, quantity))
                .flatMap(product -> getOrCreateCart(userId)
                        .flatMap(cart -> addProductAndSave(cart, product, quantity)));
    }

    public Mono<Cart> updateCartItem(String userId, String productId, int quantity) {
        return productService.getProduct(productId)
                .flatMap(product -> validateStock(product, quantity))
                .flatMap(product -> getOrCreateCart(userId))
                .flatMap(cart -> updateCartAndSave(cart, productId, quantity))
                .flatMap(this::deleteCartIfEmpty);
    }

    public Mono<Cart> removeProductFromCart(String userId, String productId) {
        return getOrCreateCart(userId)
                .flatMap(cart -> removeProductAndSave(cart, productId))
                .flatMap(this::deleteCartIfEmpty);
    }

    public Mono<BigDecimal> getCartSubtotal(String userId) {
        return getOrCreateCart(userId).map(Cart::calculateSubtotal);
    }

    public Mono<Cart> getCart(String userId) {
        return getOrCreateCart(userId);
    }

    private Mono<Cart> getOrCreateCart(String userId) {
        return cartRepository.findById(userId)
                .defaultIfEmpty(Cart.builder().userId(userId).build());
    }

    private Mono<Product> validateStock(Product product, int quantity) {
        return Mono.just(product)
                .filter(p -> p.getAvailableQuantity() >= quantity)
                .switchIfEmpty(Mono.error(new NotEnoughStockException()));
    }

    private Mono<Cart> deleteCartIfEmpty(Cart cart) {
        return cart.getItems().isEmpty()
                ? cartRepository.deleteById(cart.getUserId()).then(Mono.empty())
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
