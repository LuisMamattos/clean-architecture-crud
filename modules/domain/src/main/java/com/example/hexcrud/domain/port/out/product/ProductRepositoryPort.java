package com.example.hexcrud.domain.port.out.product;

import java.util.List;
import java.util.Optional;

import com.example.hexcrud.domain.model.product.Product;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAll();
    void delete(String id);

}