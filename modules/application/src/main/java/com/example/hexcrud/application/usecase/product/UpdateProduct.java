package com.example.hexcrud.application.usecase.product;

import java.util.Optional;

import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.port.in.product.UpdateProductUseCase;
import com.example.hexcrud.domain.port.out.product.ProductRepositoryPort;

public class UpdateProduct implements UpdateProductUseCase {
    private final ProductRepositoryPort productRepository;

    public UpdateProduct(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Output execute(Input input) {
        Optional<Product> optionalProduct = productRepository.findById(input.id());
        if (optionalProduct.isEmpty()) {
            return new Output.NotFound(input.id());
        }

        Product productToUpdate = optionalProduct.get();
        productToUpdate.updateDetails(input.newName(), input.newPrice());
        Product updatedProduct = productRepository.save(productToUpdate);

        return new Output.Updated(updatedProduct);
    }
}