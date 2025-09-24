package com.example.hexcrud.application.usecase.product;
import java.util.Optional;

import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepository;

public class UpdateProductImpl implements UpdateProduct {
    private final ProductRepository productRepository;

    public UpdateProductImpl(ProductRepository productRepository) {
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