package com.vlucenco.springframework.storeapp.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "products")
@Data
@Builder
public class Product {
    @Id
    private String id;
    private String name;
    private BigDecimal price;
    private int availableQuantity;
}
