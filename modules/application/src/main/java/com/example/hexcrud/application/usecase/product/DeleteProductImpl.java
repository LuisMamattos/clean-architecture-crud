package com.example.hexcrud.application.usecase.product;

import com.example.hexcrud.domain.exception.ResourceNotFoundException;
import com.example.hexcrud.domain.repository.product.ProductRepository;

public class DeleteProductImpl implements DeleteProduct {

    private final ProductRepository productRepository;

    public DeleteProductImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void execute(Input input) {
        if (!productRepository.existsById(input.id())) {
            throw new ResourceNotFoundException("Product not found with ID: " + input.id());
        }
        productRepository.deleteById(input.id());
    }
}