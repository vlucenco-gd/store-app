package com.vlucenco.springframework.storeapp.controller;

import com.vlucenco.springframework.storeapp.domain.Product;
import com.vlucenco.springframework.storeapp.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private ProductService productService;


    @InjectMocks
    private ProductController productController;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(productController).build();

        product1 = Product.builder().id("1").name("Product1").price(BigDecimal.valueOf(10)).build();
        product2 = Product.builder().id("2").name("Product2").price(BigDecimal.valueOf(20)).build();
    }

    @Test
    void getAllProducts() {
        BDDMockito.given(productService.getAllProducts()).willReturn(Flux.just(product1, product2));

        webTestClient.get().uri("/api/v1/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class)
                .hasSize(2);
    }

    @Test
    void getProductById() {
        BDDMockito.given(productService.getProduct(any())).willReturn(Mono.just(product1));

        webTestClient.get().uri("/api/v1/products/product1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class);
    }
}
