package com.example.hexcrud.infrastructure.repository.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.port.out.product.ProductRepositoryPort;

@Component
public class ProductRepositoryImpl implements ProductRepositoryPort {

    private final ProductMongoRepository repository;

    // CORRIGIDO: Agora o construtor pede a ferramenta correta (ProductMongoRepository)
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