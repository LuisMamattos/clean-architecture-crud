package com.example.hexcrud.domain.repository.product;

import java.util.List;
import java.util.Optional;

import com.example.hexcrud.domain.model.product.Product;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAll();
    void deleteById(String id);
    boolean existsById(String id);
    Optional<Product> findByName(String name); // Adicionado para substituir o "existsByName"
}