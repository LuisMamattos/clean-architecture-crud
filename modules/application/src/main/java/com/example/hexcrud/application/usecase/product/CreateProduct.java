package com.example.hexcrud.application.usecase.product;

import java.math.BigDecimal;

import com.example.hexcrud.domain.model.product.Product; // Import necessário

public interface CreateProduct {
    record Input(String name, BigDecimal price) {} // Alterado de double para BigDecimal
    
    Product execute(Input input);
}