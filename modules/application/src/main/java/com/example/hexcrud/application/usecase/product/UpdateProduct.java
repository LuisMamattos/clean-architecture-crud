package com.example.hexcrud.application.usecase.product;

import java.math.BigDecimal;
 
import com.example.hexcrud.domain.model.product.Product; 

public interface UpdateProduct {

    record Input(String id, String name, BigDecimal price) {} 
    Product execute(Input input);  
    
}