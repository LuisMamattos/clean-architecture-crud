package com.example.hexcrud.infrastructure.repository.product;

import java.util.List;
import java.util.Optional;

import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepositoryPort;


public class ProductRepositoryImpl implements ProductRepositoryPort {

    private final ProductMongoRepository repository;

    
    public ProductRepositoryImpl(ProductMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product product) { return repository.save(product); }

    @Override
    public Optional<Product> findById(String id) { return repository.findById(id); }

    @Override
    public List<Product> findAll() { return repository.findAll(); }

    @Override
    public void delete(String id) { repository.deleteById(id); }
   
}