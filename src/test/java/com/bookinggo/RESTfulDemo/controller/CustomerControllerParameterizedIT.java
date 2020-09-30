package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.RestfulDemoApplication;
import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.service.AbstractRESTfulDemoIT;
import org.junit.Before;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = RestfulDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integTest")
public class CustomerControllerParameterizedIT extends AbstractRESTfulDemoIT {

    private static final String LOCAL_HOST = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Sql
    @ParameterizedTest
    @MethodSource("customerIdAndReturnValueProvider")
    public void shouldReturn200_or_400_whenGetCustomerById(int customerId, int returnValue) {
        ResponseEntity<Customer> response = restTemplate
                .exchange(LOCAL_HOST + port + "/customers/" + customerId,
                        HttpMethod.GET,
                        null,
                        Customer.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(returnValue);
    }

    private static Stream<Arguments> customerIdAndReturnValueProvider() {
        return Stream.of(
                Arguments.of(1, "200"),
                Arguments.of(6, "400"),
                Arguments.of(2, "200"),
                Arguments.of(7, "400"),
                Arguments.of(3, "200"),
                Arguments.of(8, "400"),
                Arguments.of(4, "200"),
                Arguments.of(9, "400"),
                Arguments.of(3, "200"));
    }
}
