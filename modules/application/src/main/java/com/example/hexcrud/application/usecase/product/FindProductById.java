package com.example.hexcrud.application.usecase.product;

import com.example.hexcrud.domain.model.product.Product;

public interface FindProductById {
    Product execute(String id);
}