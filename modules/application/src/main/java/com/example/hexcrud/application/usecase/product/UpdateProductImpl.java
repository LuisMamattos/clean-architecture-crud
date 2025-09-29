package com.example.hexcrud.application.usecase.product;

import com.example.hexcrud.application.exception.BusinessRuleException;
import com.example.hexcrud.application.exception.ResourceNotFoundException;
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

        if (productRepository.existsByNameAndIdNot(input.name(), productToUpdate.getId())) {
            throw new BusinessRuleException("Product with name '" + input.name() + "' already exists.");
        }

        productToUpdate.updateDetails(input.name(), input.price());
        return productRepository.save(productToUpdate);
    }
}