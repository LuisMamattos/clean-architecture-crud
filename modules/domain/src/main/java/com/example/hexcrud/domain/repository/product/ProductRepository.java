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
    boolean existsByName(String email);
    boolean existsByNameAndIdNot(String email, String id);

}