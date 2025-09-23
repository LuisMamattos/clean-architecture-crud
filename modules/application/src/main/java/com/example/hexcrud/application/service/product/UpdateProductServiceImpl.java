package com.example.hexcrud.application.service.product;

import java.util.Optional;

import com.example.hexcrud.domain.model.product.Product;
import com.example.hexcrud.domain.repository.product.ProductRepositoryPort;
import com.example.hexcrud.domain.service.product.UpdateProductService;

public class UpdateProductServiceImpl implements UpdateProductService {
    private final ProductRepositoryPort productRepository;

    public UpdateProductServiceImpl(ProductRepositoryPort productRepository) {
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