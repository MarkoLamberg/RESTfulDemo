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

    private static final String CUSTOMER_TITLE = "Mr";

    private static final String CUSTOMER_NAME = "Marko Lamberg";

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepositoryMock;

    @Test
    public void shouldNotUpdateCustomer_whenUpdateCustomer_givenCustomerWithCustomerIdNonExisting() {
        Customer customer = customerService.updateCustomer(CUSTOMER_ID, CUSTOMER_TITLE, CUSTOMER_NAME);

        verify(customerRepositoryMock, times(1)).findById(CUSTOMER_ID);
        assertThat(customer).isNull();
    }

    @Test
    public void shouldCallFindAll_whenGetAllCustomers_givenNoCustomersExist() {
        List<Customer> customers = customerService.getAllCustomers();

        verify(customerRepositoryMock, times(1)).findAll();
        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    public void shouldCallFindById_whenGetCustomerById_givenNoCustomersWithIdExists() {
        Optional<Customer> customer = customerService.getCustomerById(CUSTOMER_ID);

        verify(customerRepositoryMock, times(1)).findById(CUSTOMER_ID);
        assertThat(customer).isEmpty();
    }

    @Test
    public void shouldCallFindCustomerByName_whenGetCustomerByName_givenNoCustomersWithIdExists() {
        Optional<Customer> customer = customerService.getCustomerByName(CUSTOMER_NAME);

        verify(customerRepositoryMock, times(1)).findCustomerByName(CUSTOMER_NAME);
        assertThat(customer).isEmpty();
    }

    @Test
    public void shouldCallFindById_whenGetBookingsByCustomerId_givenNoCustomersWithIdExists() {
        List<TourBooking> bookings = customerService.getBookingsByCustomerId(CUSTOMER_ID);

        verify(customerRepositoryMock, times(1)).findById(CUSTOMER_ID);
        assertThat(bookings).isNull();
    }

    @Test
    public void shouldNotDeleteAnyCustomer_whenDeleteCustomerById_givenNoCustomer() {
        customerService.deleteCustomerById(CUSTOMER_ID);

        verify(customerRepositoryMock, times(1)).deleteById(CUSTOMER_ID);
    }
}
