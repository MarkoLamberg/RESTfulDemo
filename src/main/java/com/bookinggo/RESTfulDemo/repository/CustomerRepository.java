package com.bookinggo.RestfulDemo.repository;

import com.bookinggo.RestfulDemo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findCustomerByName(String customerName);
}
