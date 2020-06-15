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

    private static final String TITLE = "Mr";

    private static final String NAME = "Marko Lamberg";

    @Autowired
    private CustomerService customerService;

    @Sql
    @Test
    public void shouldCreateCustomer_whenCreateCustomer_givenValidCustomer() {
        List<Customer> customersBefore = customerService.lookupAllCustomers();
        assertThat(customersBefore.size()).isEqualTo(5);

        customerService.createCustomer(TITLE, NAME);

        List<Customer> customersAfter = customerService.lookupAllCustomers();
        Customer customer = customersAfter.get(5);

        assertThat(customersAfter.size()).isEqualTo(6);
        assertThat(customer.getTitle()).isEqualTo(TITLE);
        assertThat(customer.getName()).isEqualTo(NAME);
    }

    @Sql
    @Test
    public void shouldReturnEightCustomers_whenLookupAllCustomers_givenCustomersExist() {
        List<Customer> customers = customerService.lookupAllCustomers();

        assertThat(customers.size()).isEqualTo(8);
    }

    @Sql
    @Test
    public void shouldReturnACustomer_whenLookupCustomerById_givenCustomerWithIdExists() {
        Optional<Customer> customers = customerService.lookupCustomerById(CUSTOMER_ID);

        assertThat(customers.isPresent()).isTrue();
    }

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenLookupBookingsByCustomerId_givenCustomerWithIdExistsAndBookingsExist() {
        List<TourBooking> tourBookings = customerService.lookupBookingsByCustomerId(CUSTOMER_ID);

        assertThat(tourBookings.size()).isEqualTo(2);
    }
}
