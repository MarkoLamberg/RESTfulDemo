package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CustomerServiceIT extends AbstractRESTfulDemoIT {

    private static final int CUSTOMER_ID = 1;

    private static final String CUSTOMER_TITLE = "Mr";

    private static final String CUSTOMER_NAME = "Marko Lamberg";

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

        assertThat(customersAfter.size()).isEqualTo(6);
        assertThat(customer.getTitle()).isEqualTo(CUSTOMER_TITLE);
        assertThat(customer.getName()).isEqualTo(CUSTOMER_NAME);
    }

    @Sql
    @Test
    public void shouldUpdateCustomer_whenUpdateCustomer_givenValidCustomer() {
        Optional<Customer> customerBefore = customerService.getCustomerById(CUSTOMER_ID);
        assertThat(customerBefore.get().getTitle()).isNotEqualTo(CUSTOMER_TITLE);
        assertThat(customerBefore.get().getName()).isNotEqualTo(CUSTOMER_NAME);

        customerService.updateCustomer(CUSTOMER_ID, CUSTOMER_TITLE, CUSTOMER_NAME);

        Optional<Customer> customerAfter = customerService.getCustomerById(CUSTOMER_ID);
        assertThat(customerAfter.get().getTitle()).isEqualTo(CUSTOMER_TITLE);
        assertThat(customerAfter.get().getName()).isEqualTo(CUSTOMER_NAME);
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
        Optional<Customer> customers = customerService.getCustomerById(CUSTOMER_ID);

        assertThat(customers).isPresent();
    }

    @Sql
    @Test
    public void shouldReturnACustomer_whenGetCustomerByName_givenCustomerWithNameExists() {
        Optional<Customer> customer = customerService.getCustomerByName(CUSTOMER_NAME);

        assertThat(customer).isPresent();
        assertThat(customer.get().getName()).isEqualTo(CUSTOMER_NAME);
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
        Optional<Customer> customerBefore = customerService.getCustomerById(CUSTOMER_ID);
        assertThat(customerBefore).isPresent();

        customerService.deleteCustomerById(CUSTOMER_ID);

        Optional<Customer> customerAfter = customerService.getCustomerById(CUSTOMER_ID);
        assertThat(customerAfter).isEmpty();
    }
}
