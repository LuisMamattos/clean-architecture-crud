package com.example.hexcrud.application.service.product;

import java.util.List;

import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepositoryPort;
import com.example.hexcrud.domain.service.product.ListAllProductsService;

public class ListAllProductsServiceImpl implements ListAllProductsService {

    private final ProductRepositoryPort productRepository;

    public ListAllProductsServiceImpl(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> execute() {
        return productRepository.findAll();
    }
}