package com.example.hexcrud.domain.service.product;

import java.util.Optional;

import com.example.hexcrud.domain.model.product.Product;

public interface FindProductByIdService {
    Optional<Product> execute(String id);
}