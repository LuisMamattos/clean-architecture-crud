package com.example.hexcrud.application.usecase.product;

import java.util.Optional;

import com.example.hexcrud.domain.model.product.Product;

public interface FindProductById {
    Optional<Product> execute(String id);
}