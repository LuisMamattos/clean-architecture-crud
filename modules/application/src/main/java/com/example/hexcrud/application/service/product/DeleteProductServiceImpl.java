package com.example.hexcrud.application.service.product;

import com.example.hexcrud.domain.repository.product.ProductRepositoryPort;
import com.example.hexcrud.domain.service.product.DeleteProductService;

public class DeleteProductServiceImpl implements DeleteProductService {

    private final ProductRepositoryPort productRepository;

    public DeleteProductServiceImpl(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Output execute(Input input) {
        return productRepository.findById(input.id())
                .map(product -> {
                    productRepository.delete(input.id());
                    return (Output) new Output.Deleted();
                })
                .orElse(new Output.NotFound(input.id()));
    }
}