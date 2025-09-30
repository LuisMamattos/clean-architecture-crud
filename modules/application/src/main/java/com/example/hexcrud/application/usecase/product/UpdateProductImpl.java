package com.example.hexcrud.application.usecase.product;

import java.util.Optional;

import com.example.hexcrud.domain.exception.BusinessRuleException;
import com.example.hexcrud.domain.exception.ResourceNotFoundException;
import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepository;

public class UpdateProductImpl implements UpdateProduct {
    private final ProductRepository productRepository;

    public UpdateProductImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(Input input) {
        Product productToUpdate = productRepository.findById(input.id())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + input.id()));

        Optional<Product> existingProduct = productRepository.findByName(input.name());
        if (existingProduct.isPresent() && !existingProduct.get().getId().equals(productToUpdate.getId())) {
             throw new BusinessRuleException("Product with name '" + input.name() + "' already exists.");
        }
        productToUpdate.updateDetails(input.name(), input.price());

        return productRepository.save(productToUpdate);
    }
}