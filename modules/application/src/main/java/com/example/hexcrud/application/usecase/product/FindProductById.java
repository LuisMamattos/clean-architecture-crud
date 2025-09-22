package com.example.hexcrud.application.usecase.product;

import java.util.Optional;

import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.port.in.product.FindProductByIdUseCase;
import com.example.hexcrud.domain.port.out.product.ProductRepositoryPort;

public class FindProductById implements FindProductByIdUseCase {

    private final ProductRepositoryPort productRepository;

    public FindProductById(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> execute(String id) {
        return productRepository.findById(id);
    }
}