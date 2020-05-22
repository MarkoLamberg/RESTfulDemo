package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.RestfulDemoApplication;
import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = RestfulDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integTest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerControllerIT {

    private static final int CUSTOMER_ID = 1;
    private static final String LOCAL_HOST = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void shouldReturnAllCustomers_whenGetAllCustomers_givenCustomersExist() {
        Customer[] customers = restTemplate
                .getForEntity(LOCAL_HOST + port + "/customers", Customer[].class)
                .getBody();
        assertEquals(8, customers.length);
    }

    @Test
    public void shouldReturnCustomer_whenGetCustomerById_givenCustomerExists() {
        Customer customer = restTemplate
                .getForEntity(LOCAL_HOST + port + "/customers/" + CUSTOMER_ID, Customer.class)
                .getBody();
        assertNotNull(customer);
        assertEquals(CUSTOMER_ID, customer.getId().intValue());
    }

    @Sql
    @Test
    public void shouldReturnBookings_whenGetCustomersBookingsById_givenBookingsExist() {
        TourBooking[] tourBookings = restTemplate
                .getForEntity(LOCAL_HOST + port + "/customers/" + CUSTOMER_ID + "/bookings", TourBooking[].class)
                .getBody();
        assertEquals(2, tourBookings.length);
    }
}
