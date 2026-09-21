package com.WebApp.repository;

import com.WebApp.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface customerRepository extends JpaRepository<Customer, Long> {
}
