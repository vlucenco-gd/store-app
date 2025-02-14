package com.vlucenco.springframework.storeapp.service;

import com.vlucenco.springframework.storeapp.domain.User;
import com.vlucenco.springframework.storeapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> registerUser(String email, String password) {
        User user = User.builder().email(email).password(password).build();
        return userRepository.save(user);
    }

    public Mono<User> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password));
    }
}
