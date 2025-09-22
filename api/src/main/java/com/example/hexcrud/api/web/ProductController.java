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
import com.example.hexcrud.domain.port.in.product.CreateProductUseCase;
import com.example.hexcrud.domain.port.in.product.DeleteProductUseCase;
import com.example.hexcrud.domain.port.in.product.FindProductByIdUseCase;
import com.example.hexcrud.domain.port.in.product.ListAllProductsUseCase;
import com.example.hexcrud.domain.port.in.product.UpdateProductUseCase;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final FindProductByIdUseCase findProductByIdUseCase;
    private final ListAllProductsUseCase listAllProductsUseCase;

    public ProductController(CreateProductUseCase createProductUseCase, UpdateProductUseCase updateProductUseCase,
                             DeleteProductUseCase deleteProductUseCase, FindProductByIdUseCase findProductByIdUseCase,
                             ListAllProductsUseCase listAllProductsUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.findProductByIdUseCase = findProductByIdUseCase;
        this.listAllProductsUseCase = listAllProductsUseCase;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        var input = new CreateProductUseCase.Input(request.name(), request.price());
        var createdProduct = createProductUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.fromDomain(createdProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody UpdateProductRequest request) {
        var input = new UpdateProductUseCase.Input(id, request.name(), request.price());
        var result = updateProductUseCase.execute(input);
        return switch (result) {
            case UpdateProductUseCase.Output.Updated res -> ResponseEntity.ok(ProductResponse.fromDomain(res.product()));
            case UpdateProductUseCase.Output.NotFound err ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found", "id", err.id()));
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        var input = new DeleteProductUseCase.Input(id);
        var result = deleteProductUseCase.execute(input);
        return switch (result) {
            case DeleteProductUseCase.Output.Deleted res -> ResponseEntity.noContent().build();
            case DeleteProductUseCase.Output.NotFound err ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found", "id", err.id()));
        };
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable String id) {
        return findProductByIdUseCase.execute(id)
                .map(ProductResponse::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAllProducts() {
        List<ProductResponse> products = listAllProductsUseCase.execute().stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }
}