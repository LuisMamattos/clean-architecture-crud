package com.example.hexcrud.api.web.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateProductRequest(
    @NotBlank(message = "Name is mandatory")
    String name, 
    @Positive(message = "Price must be positive")
    double price
) {}