package com.example.hexcrud.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.hexcrud.application.service.client.CreateClientServiceImpl;
import com.example.hexcrud.application.service.client.DeleteClientServiceImpl;
import com.example.hexcrud.application.service.client.FindClientByIdServiceImpl;
import com.example.hexcrud.application.service.client.ListAllClientsServiceImpl;
import com.example.hexcrud.application.service.client.UpdateClientServiceImpl;
import com.example.hexcrud.application.service.order.AddItemToOrderServiceImpl;
import com.example.hexcrud.application.service.order.CancelOrderServiceImpl;
import com.example.hexcrud.application.service.order.ConfirmOrderServiceImpl;
import com.example.hexcrud.application.service.order.CreateOrderServiceImpl;
import com.example.hexcrud.application.service.order.FindOrderByIdServiceImpl;
import com.example.hexcrud.application.service.order.ListAllOrdersServiceImpl;
import com.example.hexcrud.application.service.product.CreateProductServiceImpl;
import com.example.hexcrud.application.service.product.DeleteProductServiceImpl;
import com.example.hexcrud.application.service.product.FindProductByIdServiceImpl;
import com.example.hexcrud.application.service.product.ListAllProductsServiceImpl;
import com.example.hexcrud.application.service.product.UpdateProductServiceImpl;
import com.example.hexcrud.domain.repository.client.ClientRepositoryPort;
import com.example.hexcrud.domain.repository.order.OrderRepositoryPort;
import com.example.hexcrud.domain.repository.product.ProductRepositoryPort;
import com.example.hexcrud.domain.service.client.CreateClientService;
import com.example.hexcrud.domain.service.client.DeleteClientService;
import com.example.hexcrud.domain.service.client.FindClientByIdService;
import com.example.hexcrud.domain.service.client.ListAllClientsService;
import com.example.hexcrud.domain.service.client.UpdateClientService;
import com.example.hexcrud.domain.service.order.AddItemToOrderService;
import com.example.hexcrud.domain.service.order.CancelOrderService;
import com.example.hexcrud.domain.service.order.ConfirmOrderService;
import com.example.hexcrud.domain.service.order.CreateOrderService;
import com.example.hexcrud.domain.service.order.FindOrderByIdService;
import com.example.hexcrud.domain.service.order.ListAllOrdersService;
import com.example.hexcrud.domain.service.product.CreateProductService;
import com.example.hexcrud.domain.service.product.DeleteProductService;
import com.example.hexcrud.domain.service.product.FindProductByIdService;
import com.example.hexcrud.domain.service.product.ListAllProductsService;
import com.example.hexcrud.domain.service.product.UpdateProductService;
import com.example.hexcrud.infrastructure.repository.client.ClientMongoRepository;
import com.example.hexcrud.infrastructure.repository.client.ClientRepositoryImpl;
import com.example.hexcrud.infrastructure.repository.order.OrderMongoRepository;
import com.example.hexcrud.infrastructure.repository.order.OrderRepositoryImpl;
import com.example.hexcrud.infrastructure.repository.product.ProductMongoRepository;
import com.example.hexcrud.infrastructure.repository.product.ProductRepositoryImpl;

@Configuration
public class BeanConfiguration {

    // --- REPOSITORY BEANS ---
    @Bean
    public ClientRepositoryPort clientRepositoryPort(ClientMongoRepository repo) {
        return new ClientRepositoryImpl(repo);
    }

    @Bean
    public ProductRepositoryPort productRepositoryPort(ProductMongoRepository repo) {
        return new ProductRepositoryImpl(repo);
    }

    @Bean
    public OrderRepositoryPort orderRepositoryPort(OrderMongoRepository repo) {
        return new OrderRepositoryImpl(repo);
    }

    // --- CLIENT SERVICE BEANS ---
    @Bean
    public CreateClientService createClientService(ClientRepositoryPort port) {
        return new CreateClientServiceImpl(port);
    }
    
    @Bean
    public DeleteClientService deleteClientService(ClientRepositoryPort port) {
        return new DeleteClientServiceImpl(port);
    }
    
    @Bean
    public FindClientByIdService findClientByIdService(ClientRepositoryPort port) {
        return new FindClientByIdServiceImpl(port);
    }
    
    @Bean
    public ListAllClientsService listAllClientsService(ClientRepositoryPort port) {
        return new ListAllClientsServiceImpl(port);
    }
    
    @Bean
    public UpdateClientService updateClientService(ClientRepositoryPort port) {
        return new UpdateClientServiceImpl(port);
    }

    // --- PRODUCT SERVICE BEANS ---
    @Bean
    public CreateProductService createProductService(ProductRepositoryPort port) {
        return new CreateProductServiceImpl(port);
    }
    
    @Bean
    public DeleteProductService deleteProductService(ProductRepositoryPort port) {
        return new DeleteProductServiceImpl(port);
    }

    @Bean
    public FindProductByIdService findProductByIdService(ProductRepositoryPort port) {
        return new FindProductByIdServiceImpl(port);
    }

    @Bean
    public ListAllProductsService listAllProductsService(ProductRepositoryPort port) {
        return new ListAllProductsServiceImpl(port);
    }

    @Bean
    public UpdateProductService updateProductService(ProductRepositoryPort port) {
        return new UpdateProductServiceImpl(port);
    }

    // --- ORDER SERVICE BEANS ---
    @Bean
    public CreateOrderService createOrderService(OrderRepositoryPort orderPort, ClientRepositoryPort clientPort) {
        return new CreateOrderServiceImpl(orderPort, clientPort);
    }

    @Bean
    public AddItemToOrderService addItemToOrderService(OrderRepositoryPort orderPort, ProductRepositoryPort productPort) {
        return new AddItemToOrderServiceImpl(orderPort, productPort);
    }
    
    @Bean
    public ConfirmOrderService confirmOrderService(OrderRepositoryPort port) {
        return new ConfirmOrderServiceImpl(port);
    }
    
    @Bean
    public CancelOrderService cancelOrderService(OrderRepositoryPort port) {
        return new CancelOrderServiceImpl(port);
    }

    @Bean
    public FindOrderByIdService findOrderByIdService(OrderRepositoryPort port) {
        return new FindOrderByIdServiceImpl(port);
    }
    
    @Bean
    public ListAllOrdersService listAllOrdersService(OrderRepositoryPort port) {
        return new ListAllOrdersServiceImpl(port);
    }
}