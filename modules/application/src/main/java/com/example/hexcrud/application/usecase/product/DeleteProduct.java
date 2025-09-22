package com.example.hexcrud.application.usecase.product;

import com.example.hexcrud.domain.port.in.product.DeleteProductUseCase;
import com.example.hexcrud.domain.port.out.product.ProductRepositoryPort;

public class DeleteProduct implements DeleteProductUseCase {

    private final ProductRepositoryPort productRepository;

    public DeleteProduct(ProductRepositoryPort productRepository) {
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