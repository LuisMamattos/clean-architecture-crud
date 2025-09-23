package com.example.hexcrud.domain.service.product;

import com.example.hexcrud.domain.model.product.Product;

public interface CreateProductService {
    record Input(String name, double price) {}
    Product execute(Input input);
}