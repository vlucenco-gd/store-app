package com.vlucenco.springframework.storeapp.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtAuthenticationFilter extends AuthenticationWebFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, StoreAuthenticationManager storeAuthenticationManager) {
        super(storeAuthenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Optional<String> tokenOpt = extractToken(exchange);

        return tokenOpt
                .map(this::parseUserName)
                .flatMap(username -> getSecurityContext(username, tokenOpt.get()))
                .map(securityContext -> chainSecurityContext(exchange, chain, securityContext))
                .orElseGet(() -> chain.filter(exchange));
    }

    private static Mono<Void> chainSecurityContext(ServerWebExchange exchange, WebFilterChain chain, SecurityContext securityContext) {
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

    private Optional<SecurityContext> getSecurityContext(String username, String token) {

        if (username == null || !jwtUtil.validateToken(token, username)) {
            return Optional.empty();
        }

        UserDetails userDetails = new User(username, "", Collections.emptyList());
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        return Optional.of(new SecurityContextImpl(auth));
    }

    private Optional<String> extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);

        return Optional.ofNullable(authHeader)
                .filter(auth -> authHeader.startsWith("Bearer "))
                .map(auth -> auth.substring(7));
    }

    private String parseUserName(String token) {
        return jwtUtil.getUserNameFromJwtToken(token);
    }
}
