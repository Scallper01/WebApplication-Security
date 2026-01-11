package com.WebSecApp.repository;

import com.WebSecApp.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface orderRepository extends JpaRepository<Purchase, Long> {
}
