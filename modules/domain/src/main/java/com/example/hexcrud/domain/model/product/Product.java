package com.example.hexcrud.domain.model.product;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.hexcrud.domain.exception.DomainValidationException;

@Document(collection = "product")
public class Product {
    private String id;
    private String name;
    private BigDecimal price;

    private Product(String id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product create(String name, BigDecimal price) {
        validate(name, price);
        return new Product(null, name, price);
    }

    public void updateDetails(String newName, BigDecimal newPrice) {
        validate(newName, newPrice);
        this.name = newName;
        this.price = newPrice;
    }

    private static void validate(String name, BigDecimal price) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainValidationException("Product name cannot be empty.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("Product price must be positive.");
        }
    }

    // Getters públicos...
    public String getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
}