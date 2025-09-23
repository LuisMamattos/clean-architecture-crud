package com.example.hexcrud.domain.service.product;

import java.util.List;

import com.example.hexcrud.domain.model.product.Product;

public interface ListAllProductsService {
    List<Product> execute();
}