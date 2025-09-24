package com.example.hexcrud.application.usecase.product;

import com.example.hexcrud.domain.model.product.Product;

public interface CreateProduct {
    record Input(String name, double price) {}
    Product execute(Input input);
}