package com.example.hexcrud.application.service.product;

import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepositoryPort;
import com.example.hexcrud.domain.service.product.CreateProductService;

public class CreateProductServiceImpl implements CreateProductService {
    private final ProductRepositoryPort productRepository;

    public CreateProductServiceImpl(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(Input input) {
        Product newProduct = new Product(input.name(), input.price());
        return productRepository.save(newProduct);
    }
}