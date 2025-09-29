package com.example.hexcrud.application.usecase.client;

public interface DeleteClient {
    
    record Input(String id) {}    

    void execute(Input input);
}
