package com.vlucenco.springframework.storeapp.service;

import com.vlucenco.springframework.storeapp.model.Cart;
import com.vlucenco.springframework.storeapp.model.Product;
import com.vlucenco.springframework.storeapp.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    private Product product;
    private Cart cart;
    private final String sessionId = "test-session";

    @BeforeEach
    void setUp() {
        product = Product.builder().id("1").name("Sample Product")
                .price(BigDecimal.valueOf(10.00)).availableQuantity(5).build();

        cart = Cart.builder().sessionId(sessionId).build();
    }

    @Test
    void shouldAddProductWhenStockIsAvailable() {
        when(productService.getProduct("1")).thenReturn(Mono.just(product));
        when(cartRepository.findById(sessionId)).thenReturn(Mono.empty());

        when(cartRepository.save(any())).thenAnswer(invocation -> {
            Cart savedCart = invocation.getArgument(0);
            return Mono.just(savedCart);
        });

        StepVerifier.create(cartService.addProductToCart(sessionId, "1", 2))
                .expectNextMatches(cart -> cart.getItems().containsKey("1"))
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenStockIsInsufficientOnAddProduct() {
        when(productService.getProduct("1")).thenReturn(Mono.just(product));

        StepVerifier.create(cartService.addProductToCart(sessionId, "1", 10))
                .expectErrorMatches(error -> error.getMessage().contains("Not enough stock available"))
                .verify();

        verify(cartRepository, never()).findById(anyString());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void shouldDeleteCartWhenCartIsEmptyAfterItemRemoval() {
        cart.addProduct(product, 1);
        when(cartRepository.findById(sessionId)).thenReturn(Mono.just(cart));
        when(cartRepository.save(any())).thenReturn(Mono.just(Cart.builder().sessionId(sessionId).build()));
        when(cartRepository.deleteById(sessionId)).thenReturn(Mono.empty());

        StepVerifier.create(cartService.removeProductFromCart(sessionId, "1"))
                .verifyComplete();
    }

    @Test
    void shouldReturnCorrectTotal() {
        cart.addProduct(product, 2);
        when(cartRepository.findById(sessionId)).thenReturn(Mono.just(cart));

        StepVerifier.create(cartService.getCartSubtotal(sessionId))
                .expectNext(BigDecimal.valueOf(20.0))
                .verifyComplete();
    }

    @Test
    void shouldUpdateQuantityWhenStockIsAvailable() {
        cart.addProduct(product, 1);

        when(productService.getProduct("1")).thenReturn(Mono.just(product));
        when(cartRepository.findById(sessionId)).thenReturn(Mono.just(cart));
        when(cartRepository.save(any())).thenReturn(Mono.just(cart));

        StepVerifier.create(cartService.updateCartItem(sessionId, "1", 3))
                .expectNextMatches(cart -> cart.getItems().get("1").getQuantity() == 3)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenStockIsInsufficientOnUpdateProduct() {
        when(productService.getProduct("1")).thenReturn(Mono.just(product));

        StepVerifier.create(cartService.updateCartItem(sessionId, "1", 100)) // More than available
                .expectErrorMatches(error -> error.getMessage().contains("Not enough stock available"))
                .verify();
    }

    @Test
    void shouldDeleteCartWhenLastItemIsRemoved() {
        cart.addProduct(product, 1);

        when(productService.getProduct("1")).thenReturn(Mono.just(product));
        when(cartRepository.findById(sessionId)).thenReturn(Mono.just(cart));
        when(cartRepository.save(any())).thenReturn(Mono.just(Cart.builder().sessionId(sessionId).build()));
        when(cartRepository.deleteById(sessionId)).thenReturn(Mono.empty());

        StepVerifier.create(cartService.updateCartItem(sessionId, "1", 0)) // Setting quantity to 0
                .verifyComplete();
    }

}
