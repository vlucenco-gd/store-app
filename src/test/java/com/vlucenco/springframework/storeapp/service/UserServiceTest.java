package com.vlucenco.springframework.storeapp.service;

import com.vlucenco.springframework.storeapp.domain.User;
import com.vlucenco.springframework.storeapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = User.builder().email("user1@mail.moc").password("user1pass").build();
    }

    @Test
    void registerUser() {
        BDDMockito.given(userRepository.save(any()))
                .willReturn(Mono.just(user1));

        StepVerifier.create(userService.registerUser(user1.getEmail(), user1.getPassword()))
                .expectNext(user1)
                .verifyComplete();

        BDDMockito.then(userRepository).should().save(user1);
    }

    @Test
    void authenticateSuccessful() {
        BDDMockito.given(userRepository.findByEmail(user1.getEmail()))
                .willReturn(Mono.just(user1));

        StepVerifier.create(userService.authenticate(user1.getEmail(), user1.getPassword()))
                .expectNext(user1)
                .verifyComplete();

        BDDMockito.then(userRepository).should().findByEmail(user1.getEmail());
    }

    @Test
    void authenticateUnsuccessfulPwdNoMatch() {
        BDDMockito.given(userRepository.findByEmail(user1.getEmail()))
                .willReturn(Mono.just(User.builder().email("user1@mail.moc").password("NonMatchingPwd").build()));

        StepVerifier.create(userService.authenticate(user1.getEmail(), user1.getPassword()))
                .expectNext()
                .verifyComplete();

        BDDMockito.then(userRepository).should().findByEmail(user1.getEmail());
    }
}
