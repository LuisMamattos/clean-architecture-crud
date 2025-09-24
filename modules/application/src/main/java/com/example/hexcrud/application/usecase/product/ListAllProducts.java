package com.example.hexcrud.application.usecase.product;

import java.util.List;

import com.example.hexcrud.domain.model.product.Product;

public interface ListAllProducts {
    List<Product> execute();
}