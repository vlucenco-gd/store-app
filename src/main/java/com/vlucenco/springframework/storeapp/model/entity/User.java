package com.vlucenco.springframework.storeapp.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@Data
@Builder
public class User {
    @Id
    private Long id;
    private String email;
    private String password;
}
