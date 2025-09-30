package com.example.hexcrud.api.web.dto.order;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(@NotBlank String clientId) {
}