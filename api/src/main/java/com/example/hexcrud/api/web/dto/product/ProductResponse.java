package com.example.hexcrud.api.web.dto.product;

import com.example.hexcrud.domain.model.product.Product;
import java.math.BigDecimal;

public record ProductResponse(String id, String name, BigDecimal price) {
    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}