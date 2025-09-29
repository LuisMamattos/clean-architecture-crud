package com.example.hexcrud.api.web.dto.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductRequest(
    @NotBlank(message = "Name is mandatory")
    String name,

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.01", message = "Price must be positive")
    BigDecimal price
) {}