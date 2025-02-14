package com.vlucenco.springframework.storeapp.controller;

import com.vlucenco.springframework.storeapp.domain.Product;
import com.vlucenco.springframework.storeapp.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/v1/products")
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/api/v1/products/{id}")
    public Mono<Product> getProductById(@PathVariable String id) {
        return productService.getProduct(id);
    }
}
