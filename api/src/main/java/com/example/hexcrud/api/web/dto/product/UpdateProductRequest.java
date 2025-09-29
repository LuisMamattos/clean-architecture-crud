package com.example.hexcrud.api.web.dto.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateProductRequest(
    @NotBlank(message = "Name cannot be blank")
    String name,

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.01", message = "Price must be positive")
    BigDecimal price
) {}