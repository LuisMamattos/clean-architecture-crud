package com.example.hexcrud.api.config;

// Imports para as implementações da camada de aplicação (UseCaseImpl)
import com.example.hexcrud.application.usecase.client.*;
import com.example.hexcrud.application.usecase.order.*;
import com.example.hexcrud.application.usecase.product.*;
// Imports para as portas (interfaces) da camada de domínio (UseCase e Repository)
import com.example.hexcrud.domain.repository.client.ClientRepository;
import com.example.hexcrud.domain.repository.order.OrderRepository;
import com.example.hexcrud.domain.repository.product.ProductRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {      

    // --- CLIENT USE CASE BEANS ---
    @Bean
    public CreateClient createClient(ClientRepository port) {
        return new CreateClientImpl(port);
    }
    
    @Bean
    public DeleteClient deleteClient(ClientRepository port) {
        return new DeleteClientImpl(port);
    }
    
    @Bean
    public FindClientById findClientById(ClientRepository port) {
        return new FindClientByIdImpl(port);
    }
    
    @Bean
    public ListAllClients listAllClients(ClientRepository port) {
        return new ListAllClientsImpl(port);
    }
    
    @Bean
    public UpdateClient updateClient(ClientRepository port) {
        return new UpdateClientImpl(port);
    }

    // --- PRODUCT USE CASE BEANS ---
    @Bean
    public CreateProduct createProduct(ProductRepository port) {
        return new CreateProductImpl(port);
    }
    
    @Bean
    public DeleteProduct deleteProduct(ProductRepository port) {
        return new DeleteProductImpl(port);
    }

    @Bean
    public FindProductById findProductById(ProductRepository port) {
        return new FindProductByIdImpl(port);
    }

    @Bean
    public ListAllProducts listAllProducts(ProductRepository port) {
        return new ListAllProductsImpl(port);
    }

    @Bean
    public UpdateProduct updateProduct(ProductRepository port) {
        return new UpdateProductImpl(port);
    }

    // --- ORDER USE CASE BEANS ---
    @Bean
    public CreateOrder createOrder(OrderRepository orderPort, ClientRepository clientPort) {
        return new CreateOrderImpl(orderPort, clientPort);
    }

    @Bean
    public AddItemToOrder addItemToOrder(OrderRepository orderPort, ProductRepository productPort) {
        return new AddItemToOrderImpl(orderPort, productPort);
    }
    
    @Bean
    public ConfirmOrder confirmOrder(OrderRepository port) {
        return new ConfirmOrderImpl(port);
    }
    
    @Bean
    public CancelOrder cancelOrder(OrderRepository port) {
        return new CancelOrderImpl(port);
    }

    @Bean
    public FindOrderById findOrderById(OrderRepository port) {
        return new FindOrderByIdImpl(port);
    }
    
    @Bean
    public ListAllOrders listAllOrders(OrderRepository port) {
        return new ListAllOrdersImpl(port);
    }
}