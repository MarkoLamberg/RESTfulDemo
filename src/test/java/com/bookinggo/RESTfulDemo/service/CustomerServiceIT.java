package com.bookinggo.RestfulDemo.service;

import com.bookinggo.RestfulDemo.entity.Customer;
import com.bookinggo.RestfulDemo.entity.TourBooking;
import com.bookinggo.RestfulDemo.exception.CustomerServiceException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
public class CustomerServiceIT extends AbstractRestfulDemoIT {

    private static final int CUSTOMER_ID = 1;

    private static final String CUSTOMER_TITLE = "Mr";

    private static final String ORIGINAL_CUSTOMER_TITLE = "Mrs";

    private static final String CUSTOMER_NAME = "Marko Lamberg";

    private static final String ORIGINAL_CUSTOMER_NAME = "Customer One";

    @Autowired
    private CustomerService customerService;

    @Sql
    @Test
    public void shouldCreateCustomer_whenCreateCustomer_givenValidCustomer() {
        List<Customer> customersBefore = customerService.getAllCustomers();
        assertThat(customersBefore.size()).isEqualTo(5);

        customerService.createCustomer(CUSTOMER_TITLE, CUSTOMER_NAME);

        List<Customer> customersAfter = customerService.getAllCustomers();
        Customer customer = customersAfter.get(5);

        assertAll(
                () -> assertThat(customersAfter.size()).isEqualTo(6),
                () -> assertThat(customer.getTitle()).isEqualTo(CUSTOMER_TITLE),
                () -> assertThat(customer.getName()).isEqualTo(CUSTOMER_NAME));
    }

    @Sql
    @Test
    public void shouldUpdateCustomer_whenUpdateCustomer_givenValidCustomer() {
        Customer customerBefore = customerService.getCustomerById(CUSTOMER_ID);

        assertAll(
                () -> assertThat(customerBefore.getTitle()).isNotEqualTo(CUSTOMER_TITLE),
                () -> assertThat(customerBefore.getName()).isNotEqualTo(CUSTOMER_NAME));

        customerService.updateCustomer(CUSTOMER_ID, CUSTOMER_TITLE, CUSTOMER_NAME);

        Customer customerAfter = customerService.getCustomerById(CUSTOMER_ID);

        assertAll(
                () -> assertThat(customerAfter.getTitle()).isEqualTo(CUSTOMER_TITLE),
                () -> assertThat(customerAfter.getName()).isEqualTo(CUSTOMER_NAME));
    }

    @Sql
    @ParameterizedTest
    @MethodSource("titleAndNameAndCustomerProvider")
    public void parameterized_shouldUpdateCustomer_whenUpdateCustomer_givenValidCustomer(String customerTitle, String customerName, Customer updatedCustomer) {
        customerService.updateCustomer(CUSTOMER_ID, customerTitle, customerName);
        Customer customerAfter = customerService.getCustomerById(CUSTOMER_ID);

        assertAll(
                () -> assertThat(customerAfter.getTitle()).isEqualTo(updatedCustomer.getTitle()),
                () -> assertThat(customerAfter.getName()).isEqualTo(updatedCustomer.getName()));
    }

    @Sql
    @Test
    public void shouldReturnEightCustomers_whenGetAllCustomers_givenCustomersExist() {
        List<Customer> customers = customerService.getAllCustomers();
        assertThat(customers.size()).isEqualTo(8);
    }

    @Sql
    @Test
    public void shouldReturnACustomer_whenGetCustomerById_givenCustomerWithIdExists() {
        Customer customer = customerService.getCustomerById(CUSTOMER_ID);
        assertThat(customer).isNotNull();
    }

    @Sql
    @Test
    public void shouldReturnACustomer_whenGetCustomerByName_givenCustomerWithNameExists() {
        Customer customer = customerService.getCustomerByName(CUSTOMER_NAME);
        assertAll(
                () -> assertThat(customer).isNotNull(),
                () -> assertThat(customer.getName()).isEqualTo(CUSTOMER_NAME));
    }

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenGetBookingsByCustomerId_givenCustomerWithIdExistsAndBookingsExist() {
        List<TourBooking> tourBookings = customerService.getBookingsByCustomerId(CUSTOMER_ID);
        assertThat(tourBookings.size()).isEqualTo(2);
    }

    @Sql
    @Test
    public void shouldDeleteCustomer_whenDeleteCustomerById_givenCustomerWithCustomerIdExists() {
        Customer customerBefore = customerService.getCustomerById(CUSTOMER_ID);
        assertThat(customerBefore).isNotNull();

        customerService.deleteCustomerById(CUSTOMER_ID);

        assertThrows(CustomerServiceException.class,
                () -> customerService.getCustomerById(CUSTOMER_ID));
    }

    private static Stream<Arguments> titleAndNameAndCustomerProvider() {
        return Stream.of(
                Arguments.of(null, null,
                        buildCustomer(ORIGINAL_CUSTOMER_TITLE, ORIGINAL_CUSTOMER_NAME)),
                Arguments.of(CUSTOMER_TITLE, null,
                        buildCustomer(CUSTOMER_TITLE, ORIGINAL_CUSTOMER_NAME)),
                Arguments.of(null, CUSTOMER_NAME,
                        buildCustomer(ORIGINAL_CUSTOMER_TITLE, CUSTOMER_NAME)),
                Arguments.of(CUSTOMER_TITLE, CUSTOMER_NAME,
                        buildCustomer(CUSTOMER_TITLE, CUSTOMER_NAME)));
    }

    static private Customer buildCustomer(String title, String name) {
        return Customer.builder()
                .title(title)
                .name(name)
                .build();
    }
}
