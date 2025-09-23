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
import com.example.hexcrud.domain.service.product.CreateProductService;
import com.example.hexcrud.domain.service.product.DeleteProductService;
import com.example.hexcrud.domain.service.product.FindProductByIdService;
import com.example.hexcrud.domain.service.product.ListAllProductsService;
import com.example.hexcrud.domain.service.product.UpdateProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CreateProductService createProductService;
    private final UpdateProductService updateProductService;
    private final DeleteProductService deleteProductService;
    private final FindProductByIdService findProductByIdService;
    private final ListAllProductsService listAllProductsService;

    public ProductController(CreateProductService createProductService, UpdateProductService updateProductService,
                             DeleteProductService deleteProductService, FindProductByIdService findProductByIdService,
                             ListAllProductsService listAllProductsService) {
        this.createProductService = createProductService;
        this.updateProductService = updateProductService;
        this.deleteProductService = deleteProductService;
        this.findProductByIdService = findProductByIdService;
        this.listAllProductsService = listAllProductsService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        var input = new CreateProductService.Input(request.name(), request.price());
        var createdProduct = createProductService.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.fromDomain(createdProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody UpdateProductRequest request) {
        var input = new UpdateProductService.Input(id, request.name(), request.price());
        var result = updateProductService.execute(input);
        return switch (result) {
            case UpdateProductService.Output.Updated res -> ResponseEntity.ok(ProductResponse.fromDomain(res.product()));
            case UpdateProductService.Output.NotFound err ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found", "id", err.id()));
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        var input = new DeleteProductService.Input(id);
        var result = deleteProductService.execute(input);
        return switch (result) {
            case DeleteProductService.Output.Deleted res -> ResponseEntity.noContent().build();
            case DeleteProductService.Output.NotFound err ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found", "id", err.id()));
        };
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable String id) {
        return findProductByIdService.execute(id)
                .map(ProductResponse::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAllProducts() {
        List<ProductResponse> products = listAllProductsService.execute().stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }
}