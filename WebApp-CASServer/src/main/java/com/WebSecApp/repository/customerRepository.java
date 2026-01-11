package com.WebSecApp.repository;

import com.WebSecApp.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface customerRepository extends JpaRepository<Customer, Long> {
}
