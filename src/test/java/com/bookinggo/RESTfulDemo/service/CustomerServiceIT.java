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

    @Autowired
    private CustomerService customerService;

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
