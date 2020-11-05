package com.bookinggo.RestfulDemo.repository;

import com.bookinggo.RestfulDemo.RepositoryTests;
import com.bookinggo.RestfulDemo.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerRepositoryIT extends AbstractRepositoryIT implements RepositoryTests {

    private static final String CUSTOMER_NAME = "Customer One";

    @Autowired
    CustomerRepository customerRepository;

    @Sql
    @Test
    public void shouldReturnCustomer_whenFindCustomerByName_givenCustomerExists() {
        Optional<Customer> customer = customerRepository.findCustomerByName(CUSTOMER_NAME);
        assertThat(customer).isPresent();
    }
}
