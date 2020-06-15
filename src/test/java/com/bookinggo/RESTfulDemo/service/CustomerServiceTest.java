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

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    public void shouldCallFindById_whenLookupCustomerById_givenNoCustomersWithIdExists() {
        Optional<Customer> customer = customerService.lookupCustomerById(CUSTOMER_ID);
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
        assertThat(customer).isEmpty();
    }

    @Test
    public void shouldCallFindById_whenLookupBookingByCustomerId_givenNoCustomersWithIdExists() {
        List<TourBooking> bookings = customerService.lookupBookingsByCustomerId(CUSTOMER_ID);
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
        assertThat(bookings).isNull();
    }

    @Test
    public void shouldNotDeleteAnyCustomer_whenDeleteCustomerById_givenNoCustomer() {
        customerService.deleteCustomer(CUSTOMER_ID);
        verify(customerRepository, times(1)).deleteById(CUSTOMER_ID);
    }
}
