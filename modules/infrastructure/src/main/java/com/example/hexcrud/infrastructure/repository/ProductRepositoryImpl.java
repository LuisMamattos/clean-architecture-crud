package com.example.hexcrud.infrastructure.repository;

import org.springframework.data.repository.Repository;

import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepository;

public interface ProductRepositoryImpl extends Repository<Product, String>, ProductRepository {
    void deleteAll();
}