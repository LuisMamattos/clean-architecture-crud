package com.example.hexcrud.application.usecase.product;

public interface DeleteProduct {
    record Input(String id) {}

    void execute(Input input);
    
}