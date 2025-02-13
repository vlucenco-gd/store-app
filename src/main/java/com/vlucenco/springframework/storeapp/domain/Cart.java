package com.vlucenco.springframework.storeapp.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "carts")
@Data
@Builder
public class Cart {
    @Id
    private String id;
    private String userId;
    private List<String> productIds;
}
