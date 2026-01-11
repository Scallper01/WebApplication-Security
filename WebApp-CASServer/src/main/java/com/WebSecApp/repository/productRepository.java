package com.WebSecApp.repository;

import com.WebSecApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface productRepository extends JpaRepository<Product, Long> {
}
