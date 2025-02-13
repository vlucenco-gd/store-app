package com.vlucenco.springframework.storeapp.repository;

import com.vlucenco.springframework.storeapp.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
}
