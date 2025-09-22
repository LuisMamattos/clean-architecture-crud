package com.example.hexcrud.api.config;

import com.example.hexcrud.application.usecase.client.*;
import com.example.hexcrud.application.usecase.order.*;
import com.example.hexcrud.application.usecase.product.*;
import com.example.hexcrud.domain.port.in.client.*;
import com.example.hexcrud.domain.port.in.order.*;
import com.example.hexcrud.domain.port.in.product.*;
import com.example.hexcrud.domain.port.out.client.ClientRepositoryPort;
import com.example.hexcrud.domain.port.out.order.OrderRepositoryPort;
import com.example.hexcrud.domain.port.out.product.ProductRepositoryPort;
import com.example.hexcrud.infrastructure.repository.client.ClientMongoRepository;
import com.example.hexcrud.infrastructure.repository.client.ClientRepositoryImpl;
import com.example.hexcrud.infrastructure.repository.order.OrderMongoRepository;
import com.example.hexcrud.infrastructure.repository.order.OrderRepositoryImpl;
import com.example.hexcrud.infrastructure.repository.product.ProductMongoRepository;
import com.example.hexcrud.infrastructure.repository.product.ProductRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    

    @Bean
    public ClientRepositoryPort clientRepositoryPort(ClientMongoRepository clientMongoRepository) {
        return new ClientRepositoryImpl(clientMongoRepository);
    }

    @Bean
    public ProductRepositoryPort productRepositoryPort(ProductMongoRepository productMongoRepository) {
        return new ProductRepositoryImpl(productMongoRepository);
    }

    @Bean
    public OrderRepositoryPort orderRepositoryPort(OrderMongoRepository orderMongoRepository) {
        return new OrderRepositoryImpl(orderMongoRepository);
    }

   
    @Bean
    public CreateClientUseCase createClientUseCase(ClientRepositoryPort clientRepositoryPort) {
        return new CreateClient(clientRepositoryPort);
    }
    
    @Bean
    public DeleteClientUseCase deleteClientUseCase(ClientRepositoryPort clientRepositoryPort) {
        return new DeleteClient(clientRepositoryPort);
    }
    
    @Bean
    public FindClientByIdUseCase findClientByIdUseCase(ClientRepositoryPort clientRepositoryPort) {
        return new FindClientById(clientRepositoryPort);
    }
    
    @Bean
    public ListAllClientsUseCase listAllClientsUseCase(ClientRepositoryPort clientRepositoryPort) {
        return new ListAllClients(clientRepositoryPort);
    }
    
    @Bean
    public UpdateClientUseCase updateClientUseCase(ClientRepositoryPort clientRepositoryPort) {
        return new UpdateClient(clientRepositoryPort);
    }

    // PRODUCT USE CASES
    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepositoryPort productRepositoryPort) {
        return new CreateProduct(productRepositoryPort);
    }
    
    @Bean
    public DeleteProductUseCase deleteProductUseCase(ProductRepositoryPort productRepositoryPort) {
        return new DeleteProduct(productRepositoryPort);
    }

    @Bean
    public FindProductByIdUseCase findProductByIdUseCase(ProductRepositoryPort productRepositoryPort) {
        return new FindProductById(productRepositoryPort);
    }

    @Bean
    public ListAllProductsUseCase listAllProductsUseCase(ProductRepositoryPort productRepositoryPort) {
        return new ListAllProducts(productRepositoryPort);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(ProductRepositoryPort productRepositoryPort) {
        return new UpdateProduct(productRepositoryPort);
    }

    // ORDER USE CASES
    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepositoryPort orderRepositoryPort, ClientRepositoryPort clientRepositoryPort) {
        return new CreateOrder(orderRepositoryPort, clientRepositoryPort);
    }

    @Bean
    public AddItemToOrderUseCase addItemToOrderUseCase(OrderRepositoryPort orderRepositoryPort, ProductRepositoryPort productRepositoryPort) {
        return new AddItemToOrder(orderRepositoryPort, productRepositoryPort);
    }
    
    @Bean
    public ConfirmOrderUseCase confirmOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new ConfirmOrder(orderRepositoryPort);
    }
    
    @Bean
    public CancelOrderUseCase cancelOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new CancelOrder(orderRepositoryPort);
    }

    @Bean
    public FindOrderByIdUseCase findOrderByIdUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new FindOrderById(orderRepositoryPort);
    }
    
    @Bean
    public ListAllOrdersUseCase listAllOrdersUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new ListAllOrders(orderRepositoryPort);
    }
}