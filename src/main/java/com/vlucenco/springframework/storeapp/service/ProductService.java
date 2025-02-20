package com.vlucenco.springframework.storeapp.service;

import com.vlucenco.springframework.storeapp.model.Product;
import com.vlucenco.springframework.storeapp.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Flux<Product> getAllProducts() {
        return repository.findAll();
    }

    public Mono<Product> getProduct(String id) {
        return repository.findById(id);
    }
}
