package com.bookinggo.RestfulDemo.controller;

import com.bookinggo.RestfulDemo.ControllerTests;
import com.bookinggo.RestfulDemo.RestfulDemoApplication;
import com.bookinggo.RestfulDemo.entity.Customer;
import com.bookinggo.RestfulDemo.service.AbstractRestfulDemoIT;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = RestfulDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integratedTest")
public class CustomerControllerParameterizedIT extends AbstractRestfulDemoIT implements ControllerTests {

    private static final String LOCAL_HOST = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Sql
    @ParameterizedTest
    @CsvSource({
            "1, 200",
            "2, 200",
            "3, 200",
            "4, 200",
            "5, 200",
            "6, 400",
            "7, 400",
            "8, 400",
            "9, 400"
    })
    void shouldReturn200_or_400_whenGetCustomerById(int customerId, int returnValue) {
        ResponseEntity<Customer> response = restTemplate
                .exchange(LOCAL_HOST + port + "/customers/" + customerId,
                        HttpMethod.GET,
                        null,
                        Customer.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(returnValue);
    }
}
