package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    private static final int CUSTOMER_ID = 234;

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void shouldCallFindAll_whenLookupAllCustomers_givenNoCustomersExist() {
        List<Customer> customers = customerService.lookupAllCustomers();
        verify(customerRepository, times(1)).findAll();
        assertEquals(0, customers.size());
    }

    @Test
    public void shouldCallFindById_whenLookupCustomerById_givenNoCustomersWithIdExists() {
        Optional<Customer> customer = customerService.lookupCustomerById(CUSTOMER_ID);
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
        assertEquals(Optional.empty(), customer);
    }

    @Test
    public void shouldCallFindById_whenLookupBookingByCustomerId_givenNoCustomersWithIdExists() {
        List<TourBooking> bookings = customerService.lookupBookingsByCustomerId(CUSTOMER_ID);
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
        assertEquals(null, bookings);
    }
}
