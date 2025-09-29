package com.example.hexcrud.api.web.dto.client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateClientRequest(

    @NotBlank(message = "Name is mandatory")
    String name, 

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    String email

) {}