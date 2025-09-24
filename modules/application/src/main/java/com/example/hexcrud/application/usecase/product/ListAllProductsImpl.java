package com.example.hexcrud.application.usecase.product;
import java.util.List;

import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepository;

public class ListAllProductsImpl implements ListAllProducts {

    private final ProductRepository productRepository;

    public ListAllProductsImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> execute() {
        return productRepository.findAll();
    }
}