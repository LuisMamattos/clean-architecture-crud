package com.example.hexcrud.application.usecase.product;

import com.example.hexcrud.application.exception.BusinessRuleException;
import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepository;

public class CreateProductImpl implements CreateProduct {
    private final ProductRepository productRepository;

    public CreateProductImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(Input input) {
        if (productRepository.existsByName(input.name())) {
            throw new BusinessRuleException("Product with name '" + input.name() + "' already exists.");
        }

        Product newProduct = new Product(input.name(), input.price());
        return productRepository.save(newProduct);
    }
}