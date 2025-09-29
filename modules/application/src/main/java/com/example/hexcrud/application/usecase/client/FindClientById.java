package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.model.client.Client;

public interface FindClientById {
   Client execute(String id);
}