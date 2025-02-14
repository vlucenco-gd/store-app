package com.vlucenco.springframework.storeapp.bootstrap;

import com.vlucenco.springframework.storeapp.domain.Product;
import com.vlucenco.springframework.storeapp.domain.User;
import com.vlucenco.springframework.storeapp.repository.ProductRepository;
import com.vlucenco.springframework.storeapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Bootstrap implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Bootstrap(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count().block() == 0) {
            loadProducts();
        }

        if (userRepository.count().block() == 0) {
            loadUsers();
        }
    }

    private void loadProducts() {
        System.out.println("#### Loading Products Bootstrap Data ####");

        productRepository.save(Product.builder()
                        .name("Table").price(BigDecimal.valueOf(100)).build())
                .block();

        productRepository.save(Product.builder()
                        .name("Chair").price(BigDecimal.valueOf(30)).build())
                .block();

        productRepository.save(Product.builder()
                        .name("Lamp").price(BigDecimal.valueOf(20)).build())
                .block();

        System.out.println("Loaded Products: " + productRepository.count().block());
    }

    private void loadUsers() {
        System.out.println("#### Loading Users Bootstrap Data ####");

        userRepository.save(User.builder()
                        .email("user1@email.moc").password("user1pas123").build())
                .block();

        userRepository.save(User.builder()
                        .email("user2@email.moc").password("user2pas123").build())
                .block();

        System.out.println("Loaded Users: " + userRepository.count().block());
    }
}
