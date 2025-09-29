package com.example.hexcrud.application.usecase.product;

import com.example.hexcrud.application.exception.ResourceNotFoundException;
import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepository;

public class FindProductByIdImpl implements FindProductById {

    private final ProductRepository productRepository;

    public FindProductByIdImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }
}