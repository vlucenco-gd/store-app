package com.vlucenco.springframework.storeapp.service;

import com.vlucenco.springframework.storeapp.model.User;
import com.vlucenco.springframework.storeapp.exception.UserAlreadyExistsException;
import com.vlucenco.springframework.storeapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public Mono<User> registerUser(String email, String password) {
        return userRepository.findByEmail(email)
                .flatMap(existingUser -> Mono.<User>error(UserAlreadyExistsException::new))
                .switchIfEmpty(Mono.defer(() -> register(email, password)));
    }

    private Mono<User> register(String email, String password) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        return userRepository.save(user);
    }

    public Mono<User> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }
}
