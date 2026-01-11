package com.WebApp.web;

import com.WebApp.entities.Product;
import com.WebApp.service.SecAppService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecAppController {
    @Autowired
    private SecAppService service;

    @GetMapping(path = "/product")
    public Optional<Product> getProductById(@RequestParam(name = "Id") Long id){
        return service.getProductById(id);
    }

    @GetMapping(path = "/products")
    public List<Product> getAllProducts(){
        return service.getAllProducts();
    }

    @PostMapping(path = "/product")
    public Product addProduct(@RequestBody Product product){
        return service.addProduct(product);
    }

    @GetMapping("/debug/auth")
    public Object auth(Authentication authentication) {

        return authentication.getAuthorities();
    }

}
