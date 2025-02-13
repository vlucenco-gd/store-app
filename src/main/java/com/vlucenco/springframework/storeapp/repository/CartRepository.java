package com.vlucenco.springframework.storeapp.repository;

import com.vlucenco.springframework.storeapp.domain.Cart;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CartRepository extends ReactiveMongoRepository<Cart, String> {
}
