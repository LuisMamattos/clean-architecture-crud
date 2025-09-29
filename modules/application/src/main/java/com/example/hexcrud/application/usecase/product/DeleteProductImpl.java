package com.example.hexcrud.application.usecase.product;
import com.example.hexcrud.domain.repository.product.ProductRepository;

public class DeleteProductImpl implements DeleteProduct {

    private final ProductRepository productRepository;

    public DeleteProductImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Output execute(Input input) {
        return productRepository.findById(input.id())
                .map(product -> {
                    productRepository.deleteById(input.id());
                    return (Output) new Output.Deleted();
                })
                .orElse(new Output.NotFound(input.id()));
    }
}