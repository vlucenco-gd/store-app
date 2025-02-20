package com.vlucenco.springframework.storeapp.repository;

import com.vlucenco.springframework.storeapp.model.entity.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
