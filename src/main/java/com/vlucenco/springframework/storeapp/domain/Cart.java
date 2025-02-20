package com.vlucenco.springframework.storeapp.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@Document(collection = "carts")
public class Cart {
    @Id
    private String sessionId;
    private Map<String, CartItem> items;

    public void addProduct(Product product, int quantity) {
        items.compute(product.getId(), (id, existingItem) -> {
            if (existingItem == null) {
                return new CartItem(product, quantity);
            } else {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                return existingItem;
            }
        });
    }

    public void updateProduct(String productId, int quantity) {
        if (items.containsKey(productId)) {
            if (quantity > 0) {
                items.get(productId).setQuantity(quantity);
            } else {
                items.remove(productId);
            }
        }
    }

    public void removeProduct(String productId) {
        items.remove(productId);
    }

    public BigDecimal calculateSubtotal() {
        return items.values().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
