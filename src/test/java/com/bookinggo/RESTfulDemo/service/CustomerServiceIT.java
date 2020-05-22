package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class CustomerServiceIT extends AbstractRESTfulDemoIT {

    private static final int CUSTOMER_ID = 1;

    @Autowired
    private CustomerService service;

    @Test
    public void shouldReturnAllCustomers_whenLookupAllCustomers_givenCustomersExist() {
        List<Customer> customers = service.lookupAllCustomers();

        assertEquals(8, customers.size());
    }

    @Test
    public void shouldReturnACustomer_whenLookupCustomerById_givenCustomerWithIdExists() {
        Optional<Customer> customer = service.lookupCustomerById(CUSTOMER_ID);

        assertEquals(true, customer.isPresent());
    }

    @Sql
    @Test
    public void shouldReturnBookings_whenLookupBookingsByCustomerId_givenCustomerWithIdExistsAndBookingsExist() {
        List<TourBooking> tourBookings = service.lookupBookingsByCustomerId(CUSTOMER_ID);

        assertEquals(2, tourBookings.size());
    }
}
