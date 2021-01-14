package com.bookinggo.RestfulDemo.service;

import com.bookinggo.RestfulDemo.ServiceTests;
import com.bookinggo.RestfulDemo.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(properties = {"user.authenticate=true", "user.authentication.url=http://localhost:1090"})
public class CustomerServiceAuthenticationIT extends AbstractRestfulDemoIT implements ServiceTests {

    @Autowired
    private CustomerService customerService;

    @Test
    public void shouldCreateCustomer_whenCreateCustomer_givenValidAuthentication() {
        Customer customer = customerService.createCustomer("mr", "Lamberg");

        assertThat(customer).isNotNull();
        assertThat(customer.getTitle()).isEqualTo("mr");
    }
}