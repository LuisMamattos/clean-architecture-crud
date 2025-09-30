package com.example.hexcrud.api.web;

import java.util.List;
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
import com.example.hexcrud.domain.model.product.Product;

import jakarta.validation.Valid;

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
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
        var input = new CreateProduct.Input(request.name(), request.price());
        Product createdProduct = createProduct.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.fromDomain(createdProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable String id, @RequestBody @Valid UpdateProductRequest request) {
        var input = new UpdateProduct.Input(id, request.name(), request.price());
        Product updatedProduct = updateProduct.execute(input);
        return ResponseEntity.ok(ProductResponse.fromDomain(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deleteProduct.execute(new DeleteProduct.Input(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable String id) {
        Product product = findProductById.execute(id);
        return ResponseEntity.ok(ProductResponse.fromDomain(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAll() {
        List<ProductResponse> products = listAllProducts.execute().stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }
}