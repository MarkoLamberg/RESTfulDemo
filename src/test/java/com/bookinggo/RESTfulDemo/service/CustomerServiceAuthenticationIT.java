package com.bookinggo.RestfulDemo.service;

import com.bookinggo.RestfulDemo.ServiceTests;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(properties = {"user.authentication.do=true", "user.authentication.url=http://localhost:1090"})
public class CustomerServiceAuthenticationIT extends AbstractRestfulDemoIT implements ServiceTests {

    @Autowired
    private CustomerService customerService;

    /*@Test
    public void shouldCreateCustomer_whenCreateCustomer_givenValidAuthentication() {
        Customer customer = customerService.createCustomer("mr", "Lamberg");

        assertThat(customer).isNotNull();
        assertThat(customer.getTitle()).isEqualTo("mr");
    }*/
}