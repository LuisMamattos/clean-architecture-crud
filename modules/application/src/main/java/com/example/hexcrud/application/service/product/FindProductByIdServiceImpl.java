package com.example.hexcrud.application.service.product;

import java.util.Optional;

import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepositoryPort;
import com.example.hexcrud.domain.service.product.FindProductByIdService;

public class FindProductByIdServiceImpl implements FindProductByIdService {

    private final ProductRepositoryPort productRepository;

    public FindProductByIdServiceImpl(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> execute(String id) {
        return productRepository.findById(id);
    }
}