package com.example.hexcrud.application.usecase.product;
import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepository;

public class CreateProductImpl implements CreateProduct {
    private final ProductRepository productRepository;

    public CreateProductImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(Input input) {
        Product newProduct = new Product(input.name(), input.price());
        return productRepository.save(newProduct);
    }
}