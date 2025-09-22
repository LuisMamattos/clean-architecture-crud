package com.example.hexcrud.api.web.dto.product;

import com.example.hexcrud.domain.model.product.Product;

public record ProductResponse(String id, String name, double price) {
    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}