package com.example.hexcrud.api.web.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AddItemToOrderRequest(
    @NotBlank String productId,
    @Min(1) int quantity
) {
}