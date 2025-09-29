package com.example.hexcrud.infrastructure.repository;

import org.springframework.data.repository.Repository;

import com.example.hexcrud.domain.model.order.Order;
import com.example.hexcrud.domain.repository.order.OrderRepository;

public interface OrderRepositoryImpl extends Repository<Order, String>, OrderRepository {}