package com.WebApp.service;

import com.WebApp.entities.Product;
import com.WebApp.repository.customerRepository;
import com.WebApp.repository.orderRepository;
import com.WebApp.repository.productRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecAppService {
    @Autowired
    private productRepository productRepository;
    @Autowired
    private customerRepository customerRepository;
    @Autowired
    private orderRepository orderRepository;


    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product addProduct(Product product){
        return productRepository.save(product);
    }
}
