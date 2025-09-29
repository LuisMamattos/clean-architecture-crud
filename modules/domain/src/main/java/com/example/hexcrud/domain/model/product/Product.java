package com.example.hexcrud.domain.model.product;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

    private String id;
    private String name;
    private BigDecimal price;

    public Product() {}    
    public Product(String name, BigDecimal price) {        
        this.name = name;
        this.price = price;
    }
    public Product(String id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public static Product create(String name, BigDecimal price) {
        validate(name, price);
        String newId = UUID.randomUUID().toString();
        return new Product(newId, name, price);
    }
    public void updateDetails(String newName, BigDecimal newPrice) {
        validate(newName, newPrice);
        this.name = newName;
        this.price = newPrice;
    }
    private static void validate(String name, BigDecimal price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be positive.");
        }
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    
    
}