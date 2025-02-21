package com.vlucenco.springframework.storeapp.model.dto;

import com.vlucenco.springframework.storeapp.model.entity.Cart;
import com.vlucenco.springframework.storeapp.model.entity.CartItem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartResponse {

    private String userId;
    private List<CartItemResponse> items;
    private BigDecimal subtotal;

    public static CartResponse from(Cart cart) {
        return CartResponse.builder()
                .userId(cart.getUserId())
                .items(cart.getItems().values().stream()
                        .map(CartItemResponse::fromCartItem)
                        .toList())
                .subtotal(cart.calculateSubtotal())
                .build();
    }

    @Data
    @Builder
    public static class CartItemResponse {
        private int itemRef;
        private String productId;
        private String productName;
        private int quantity;
        private BigDecimal price;

        public static CartItemResponse fromCartItem(CartItem cartItem) {
            return CartItemResponse.builder()
                    .itemRef(cartItem.getItemRef())
                    .productId(cartItem.getProduct().getId())
                    .productName(cartItem.getProduct().getName())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getProduct().getPrice())
                    .build();
        }
    }
}
