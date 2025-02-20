package com.vlucenco.springframework.storeapp.repository;

import com.vlucenco.springframework.storeapp.model.entity.Cart;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends ReactiveMongoRepository<Cart, String> {
}
