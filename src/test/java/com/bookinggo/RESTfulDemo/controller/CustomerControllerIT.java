package com.bookinggo.RestfulDemo.controller;

import com.bookinggo.RestfulDemo.ControllerTests;
import com.bookinggo.RestfulDemo.RestfulDemoApplication;
import com.bookinggo.RestfulDemo.dto.CustomerDto;
import com.bookinggo.RestfulDemo.dto.CustomerPatchDto;
import com.bookinggo.RestfulDemo.entity.Customer;
import com.bookinggo.RestfulDemo.entity.TourBooking;
import com.bookinggo.RestfulDemo.service.AbstractRestfulDemoIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(classes = RestfulDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integratedTest")
public class CustomerControllerIT extends AbstractRestfulDemoIT implements ControllerTests {

    private static final int CUSTOMER_ID = 1;
    private static final int NON_EXISTING_CUSTOMER_ID = 20;
    private static final String LOCAL_HOST = "http://localhost:";
    private static final String CUSTOMER_TITLE = "Mr";
    private static final String CUSTOMER_NAME = "Marko Lamberg";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturn201_whenCustomerCreated_givenValidCustomer() {
        ResponseEntity<Customer> response = restTemplate
                .postForEntity(LOCAL_HOST + port + "/customers",
                        buildCustomerDto(),
                        Customer.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(CREATED.value());
    }

    @Sql
    @Test
    public void shouldReturn400_whenCustomerCreated_givenCustomerWithThatNameAlreadyExists() {
        ResponseEntity<Customer> response = restTemplate
                .postForEntity(LOCAL_HOST + port + "/customers",
                        buildCustomerDto(),
                        Customer.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturn200_whenCustomerUpdated_givenValidCustomer() {
        ResponseEntity<Customer> response = restTemplate
                .exchange(LOCAL_HOST + port + "/customers/" + CUSTOMER_ID,
                        HttpMethod.PUT,
                        new HttpEntity<>(CustomerPatchDto.builder()
                                .title(CUSTOMER_TITLE)
                                .name(CUSTOMER_NAME)
                                .build()),
                        Customer.class);

        assertAll(
                () -> assertThat(response.getStatusCodeValue()).isEqualTo(OK.value()),
                () -> assertThat(response.getBody().getTitle()).isEqualTo(CUSTOMER_TITLE),
                () -> assertThat(response.getBody().getName()).isEqualTo(CUSTOMER_NAME));
    }

    @Sql
    @Test
    public void shouldReturn200_whenCustomerUpdatedSome_givenValidCustomer() {
        ResponseEntity<Customer> response = restTemplate
                .exchange(LOCAL_HOST + port + "/customers/" + CUSTOMER_ID,
                        HttpMethod.PUT,
                        new HttpEntity<>(CustomerPatchDto.builder()
                                .title(CUSTOMER_TITLE)
                                .build()),
                        Customer.class);

        assertAll(
                () -> assertThat(response.getStatusCodeValue()).isEqualTo(OK.value()),
                () -> assertThat(response.getBody().getTitle()).isEqualTo(CUSTOMER_TITLE),
                () -> assertThat(response.getBody().getName()).isEqualTo(CUSTOMER_NAME));
    }

    @Sql
    @Test
    public void shouldReturn400_whenCustomerUpdated_givenCustomerWithIdDoesntExist() {
        ResponseEntity<Customer> response = restTemplate
                .exchange(LOCAL_HOST + port + "/customers/" + NON_EXISTING_CUSTOMER_ID,
                        HttpMethod.PUT,
                        new HttpEntity<>(buildCustomerPatchDto()),
                        Customer.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturn400_whenCustomerUpdated_givenCustomerWithNewNameExists() {
        ResponseEntity<Customer> response = restTemplate
                .exchange(LOCAL_HOST + port + "/customers/" + CUSTOMER_ID,
                        HttpMethod.PUT,
                        new HttpEntity<>(buildCustomerPatchDto()),
                        Customer.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
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

        assertAll(
                () -> assertThat(customer).isNotNull(),
                () -> assertThat(customer.getId().intValue()).isEqualTo(CUSTOMER_ID));
    }

    @Sql
    @Test
    public void shouldReturn400_whenGetCustomerById_givenCustomerDoesntExist() {
        ResponseEntity<Customer> response = restTemplate
                .exchange(LOCAL_HOST + port + "/customers/" + NON_EXISTING_CUSTOMER_ID,
                        HttpMethod.GET,
                        null,
                        Customer.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenGetCustomersBookingsById_givenBookingsExist() {
        TourBooking[] tourBookings = restTemplate
                .getForEntity(LOCAL_HOST + port + "/customers/" + CUSTOMER_ID + "/bookings", TourBooking[].class)
                .getBody();

        assertThat(tourBookings.length).isEqualTo(2);
    }

    @Sql
    @Test
    public void shouldReturn400_whenGetCustomersBookingsById_givenCustomerDoesntExist() {
        ResponseEntity<String> response = restTemplate
                .exchange(LOCAL_HOST + port + "/customers/" + NON_EXISTING_CUSTOMER_ID + "/bookings",
                        HttpMethod.GET,
                        null,
                        String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldDeleteCustomer_whenDeleteCustomer_givenCustomerExists() {
        Customer[] customersBefore = restTemplate
                .getForEntity(LOCAL_HOST + port + "/customers", Customer[].class)
                .getBody();

        assertThat(customersBefore.length).isEqualTo(4);

        restTemplate.delete(LOCAL_HOST + port + "/customers/" + CUSTOMER_ID);

        Customer[] customersAfter = restTemplate
                .getForEntity(LOCAL_HOST + port + "/customers", Customer[].class)
                .getBody();

        assertThat(customersAfter.length).isEqualTo(3);
    }

    @Test
    public void shouldReturn400_whenDeleteCustomer_givenCustomerWithIdDoesntExist() {
        ResponseEntity<Customer> response = restTemplate
                .exchange(LOCAL_HOST + port + "/customers/" + NON_EXISTING_CUSTOMER_ID,
                        HttpMethod.DELETE,
                        null,
                        Customer.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    private CustomerPatchDto buildCustomerPatchDto() {
        return CustomerPatchDto.builder()
                .name(CUSTOMER_NAME)
                .build();
    }

    private CustomerDto buildCustomerDto() {
        return CustomerDto.builder()
                .title(CUSTOMER_TITLE)
                .name(CUSTOMER_NAME)
                .build();
    }
}
