package com.example.hexcrud.api.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hexcrud.api.web.dto.product.CreateProductRequest;
import com.example.hexcrud.api.web.dto.product.ProductResponse;
import com.example.hexcrud.api.web.dto.product.UpdateProductRequest;
import com.example.hexcrud.application.usecase.product.CreateProduct;
import com.example.hexcrud.application.usecase.product.DeleteProduct;
import com.example.hexcrud.application.usecase.product.FindProductById;
import com.example.hexcrud.application.usecase.product.ListAllProducts;
import com.example.hexcrud.application.usecase.product.UpdateProduct;

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
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        var input = new CreateProduct.Input(request.name(), request.price());
        var createdProduct = createProduct.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.fromDomain(createdProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody UpdateProductRequest request) {
        var input = new UpdateProduct.Input(id, request.name(), request.price());
        var result = updateProduct.execute(input);
        return switch (result) {
            case UpdateProduct.Output.Updated res -> ResponseEntity.ok(ProductResponse.fromDomain(res.product()));
            case UpdateProduct.Output.NotFound err ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found", "id", err.id()));
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        var input = new DeleteProduct.Input(id);
        var result = deleteProduct.execute(input);
        return switch (result) {
            case DeleteProduct.Output.Deleted res -> ResponseEntity.noContent().build();
            case DeleteProduct.Output.NotFound err ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found", "id", err.id()));
        };
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable String id) {
        return findProductById.execute(id)
                .map(ProductResponse::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAllProducts() {
        List<ProductResponse> products = listAllProducts.execute().stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }
}