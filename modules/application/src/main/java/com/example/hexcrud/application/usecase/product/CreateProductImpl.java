package com.example.hexcrud.application.usecase.product;

import com.example.hexcrud.domain.exception.BusinessRuleException;
import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepository;

public class CreateProductImpl implements CreateProduct {
    private final ProductRepository productRepository;

    public CreateProductImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(Input input) {
        // 1. ORCHESTRATE
        if (productRepository.findByName(input.name()).isPresent()) {
            throw new BusinessRuleException("Product with name '" + input.name() + "' already exists.");
        }

        // 2. EXECUTE
        Product newProduct = Product.create(input.name(), input.price());

        // 3. SAVE
        return productRepository.save(newProduct);
    }
}