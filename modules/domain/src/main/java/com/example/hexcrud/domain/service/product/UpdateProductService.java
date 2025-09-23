package com.example.hexcrud.domain.service.product;

import com.example.hexcrud.domain.model.product.Product;

public interface UpdateProductService {
    record Input(String id, String newName, double newPrice) {}

    sealed interface Output {
        record Updated(Product product) implements Output {}
        record NotFound(String id) implements Output {}
    }

    Output execute(Input input);
}