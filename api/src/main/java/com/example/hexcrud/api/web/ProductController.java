package com.example.hexcrud.api.web;

import com.example.hexcrud.api.web.dto.product.CreateProductRequest;
import com.example.hexcrud.api.web.dto.product.ProductResponse;
import com.example.hexcrud.api.web.dto.product.UpdateProductRequest;
import com.example.hexcrud.application.usecase.product.*;
import com.example.hexcrud.domain.model.product.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CreateProduct createProduct;
    private final UpdateProduct updateProduct;
    private final DeleteProduct deleteProduct;
    private final FindProductById findProductById;
    private final ListAllProducts listAllProducts;

    public ProductController(CreateProduct createProduct, UpdateProduct updateProduct,
                             DeleteProduct deleteProduct, FindProductById findProductById,
                             ListAllProducts listAllProducts) {
        this.createProduct = createProduct;
        this.updateProduct = updateProduct;
        this.deleteProduct = deleteProduct;
        this.findProductById = findProductById;
        this.listAllProducts = listAllProducts;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request) {
        var input = new CreateProduct.Input(request.name(), request.price());
        Product createdProduct = createProduct.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.fromDomain(createdProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable String id, @RequestBody @Valid UpdateProductRequest request) {
        var input = new UpdateProduct.Input(id, request.name(), request.price());
        Product updatedProduct = updateProduct.execute(input);
        return ResponseEntity.ok(ProductResponse.fromDomain(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        var input = new DeleteProduct.Input(id);
        deleteProduct.execute(input);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable String id) {
        Product product = findProductById.execute(id);
        return ResponseEntity.ok(ProductResponse.fromDomain(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAllProducts() {
        List<ProductResponse> products = listAllProducts.execute().stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }
}