package com.vlucenco.springframework.storeapp.service;

import com.vlucenco.springframework.storeapp.domain.Product;
import com.vlucenco.springframework.storeapp.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    public static final String ID_ONE = "1";
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = Product.builder().id(ID_ONE).name("Product1").price(BigDecimal.valueOf(10)).build();
        product2 = Product.builder().id("2").name("Product2").price(BigDecimal.valueOf(20)).build();
    }

    @Test
    void getAllProducts() {
        BDDMockito.given(productRepository.findAll()).willReturn(Flux.just(product1, product2));

        StepVerifier.create(productService.getAllProducts())
                .expectNext(product1, product2)
                .verifyComplete();

        BDDMockito.then(productRepository).should(times(1)).findAll();
    }

    @Test
    void getProductFound() {
        BDDMockito.given(productRepository.findById(ID_ONE)).willReturn(Mono.just(product1));

        StepVerifier.create(productService.getProduct(ID_ONE))
                .expectNext(product1)
                .verifyComplete();

        BDDMockito.then(productRepository).should().findById(ID_ONE);
    }

    @Test
    void getProductNotFound() {
        BDDMockito.given(productRepository.findById("123")).willReturn(Mono.empty());

        StepVerifier.create(productService.getProduct("123"))
                .verifyComplete();

        BDDMockito.then(productRepository).should().findById("123");
    }
}
