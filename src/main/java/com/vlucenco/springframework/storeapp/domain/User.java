package com.vlucenco.springframework.storeapp.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@Data
public class User {
    @Id
    private String id;
    private String email;
    private String password;
}
