package com.vlucenco.springframework.storeapp.controller;

import com.vlucenco.springframework.storeapp.model.entity.User;
import com.vlucenco.springframework.storeapp.security.JwtUtil;
import com.vlucenco.springframework.storeapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class UserAuthControllerTest {

    public static final String AUTH_PATH = "/api/v1/auth";

    private WebTestClient webTestClient;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserAuthController userAuthController;

    private User user1;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(userAuthController).build();

        user1 = User.builder().email("user@mail.moc").password("user1pas123").build();
    }

    @Test
    void register() {
        BDDMockito.given(userService.registerUser(anyString(), anyString()))
                .willReturn(Mono.just(user1));

        webTestClient.post()
                .uri(AUTH_PATH + "/register?email=user@mail.moc&password=user1pas123")
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void login() {
        BDDMockito.given(userService.authenticate(anyString(), anyString()))
                .willReturn(Mono.just(user1));
        BDDMockito.given(jwtUtil.generateToken(anyString()))
                .willReturn("token");

        webTestClient.post()
                .uri(AUTH_PATH + "/login?email=user@mail.moc&password=user1pas123")
                .exchange()
                .expectStatus().isOk();

        BDDMockito.verify(userService).authenticate(anyString(), anyString());
        BDDMockito.verify(jwtUtil).generateToken(anyString());
    }
}
