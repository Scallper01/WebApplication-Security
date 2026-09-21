package com.WebApp.repository;

import com.WebApp.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface orderRepository extends JpaRepository<Purchase, Long> {
}
