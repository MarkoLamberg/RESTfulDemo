package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.RestfulDemoApplication;
import com.bookinggo.RESTfulDemo.dto.CustomerDto;
import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.AbstractRESTfulDemoIT;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest(classes = RestfulDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integTest")
public class CustomerControllerIT extends AbstractRESTfulDemoIT {

    private static final int CUSTOMER_ID = 1;

    private static final String LOCAL_HOST = "http://localhost:";

    private static final String TITLE = "Mr";

    private static final String NAME = "Marko Lamberg";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Sql
    @Test
    public void shouldReturnFourCustomers_whenGetAllCustomers_givenCustomersExist() {
        Customer[] customers = restTemplate
                .getForEntity(LOCAL_HOST + port + "/customers", Customer[].class)
                .getBody();

        assertThat(customers.length).isEqualTo(4);
    }

    @Sql
    @Test
    public void shouldReturnCustomer_whenGetCustomerById_givenCustomerExists() {
        Customer customer = restTemplate
                .getForEntity(LOCAL_HOST + port + "/customers/" + CUSTOMER_ID, Customer.class)
                .getBody();

        assertThat(customer).isNotNull();
        assertThat(customer.getId().intValue()).isEqualTo(CUSTOMER_ID);
    }

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenGetCustomersBookingsById_givenBookingsExist() {
        TourBooking[] tourBookings = restTemplate
                .getForEntity(LOCAL_HOST + port + "/customers/" + CUSTOMER_ID + "/bookings", TourBooking[].class)
                .getBody();

        assertThat(tourBookings.length).isEqualTo(2);
    }

    @Test
    public void shouldReturn201_whenCustomerCreated_givenValidCustomer() {
        CustomerDto customerDto = CustomerDto.builder()
                .title(TITLE)
                .name(NAME)
                .build();

        ResponseEntity<Customer> response = restTemplate
                .postForEntity(LOCAL_HOST + port + "/customers", customerDto, Customer.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(CREATED.value());
    }
}
