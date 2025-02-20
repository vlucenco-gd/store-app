package com.vlucenco.springframework.storeapp.bootstrap;

import com.vlucenco.springframework.storeapp.domain.Product;
import com.vlucenco.springframework.storeapp.repository.ProductRepository;
import com.vlucenco.springframework.storeapp.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

@Component
public class Bootstrap implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserService userService;

    public Bootstrap(ProductRepository productRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count().block() == 0) {
            loadProducts();
        }

        if (CollectionUtils.isEmpty(userService.getAllUsers().collectList().block())) {
            loadUsers();
        }
    }

    private void loadProducts() {
        System.out.println("#### Loading Products Bootstrap Data ####");

        productRepository.save(Product.builder()
                        .name("Table").price(BigDecimal.valueOf(100)).availableQuantity(10).build())
                .block();

        productRepository.save(Product.builder()
                        .name("Chair").price(BigDecimal.valueOf(30)).availableQuantity(10).build())
                .block();

        productRepository.save(Product.builder()
                        .name("Lamp").price(BigDecimal.valueOf(20)).availableQuantity(10).build())
                .block();

        System.out.println("Loaded Products: " + productRepository.count().block());
    }

    private void loadUsers() {
        System.out.println("#### Loading Users Bootstrap Data ####");

        userService.registerUser("user1@email.moc", "user1pas123").block();
        userService.registerUser("user2@email.moc", "user2pas123").block();

        System.out.println("Loaded Users: " + userService.getAllUsers().count().block());
    }
}
