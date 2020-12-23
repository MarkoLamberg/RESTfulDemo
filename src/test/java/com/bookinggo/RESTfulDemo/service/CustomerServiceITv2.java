package com.bookinggo.RestfulDemo.service;

import com.bookinggo.RestfulDemo.ServiceTests;
import com.bookinggo.RestfulDemo.entity.Customer;
import com.bookinggo.RestfulDemo.exception.CustomerServiceException;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@SpringBootTest(properties = {"user.authenticate=true", "user.authentication.url=http://localhost:8060"})
@AutoConfigureWireMock(port = 8060, files = "classpath*:/wiremock")
public class CustomerServiceITv2 extends AbstractRestfulDemoIT implements ServiceTests {

    private static final String CUSTOMER_TITLE = "Mr";
    private static final String CUSTOMER_NAME = "Marko Lamberg";

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        WireMock.reset();
    }

    @Test
    public void shouldCreateCustomer_whenCreateCustomer_givenValidAuthentication() {
        stubFor(post(urlEqualTo("/authentication"))
                .willReturn(aResponse()
                        .withStatus(202)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                )
        );

        Customer customer = customerService.createCustomer(CUSTOMER_TITLE, CUSTOMER_NAME);
        assertThat(customer).isNotNull();
    }

    @Test
    public void shouldNotCreateCustomer_whenCreateCustomer_givenInvalidAuthentication() {
        stubFor(post(urlEqualTo("/authentication"))
                .willReturn(aResponse()
                        .withStatus(203)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                )
        );

        assertThrows(CustomerServiceException.class,
                () -> customerService.createCustomer(CUSTOMER_TITLE, CUSTOMER_NAME));
    }
}
