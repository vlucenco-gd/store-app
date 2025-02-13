package com.vlucenco.springframework.storeapp.repository;

import com.vlucenco.springframework.storeapp.domain.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
