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

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        Optional<Customer> customer = customerService.updateCustomer(CUSTOMER_ID, CUSTOMER_TITLE, CUSTOMER_NAME);

        verify(customerRepositoryMock, times(1)).findById(CUSTOMER_ID);
        assertThat(customer).isEmpty();
    }

    @Test
    public void shouldUpdateACustomer_whenUpdateCustomer_givenCustomerWithCustomerIdExists() {
        Customer customer = spy(Customer
                .builder()
                .id(CUSTOMER_ID)
                .build());
        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(customerRepositoryMock.saveAndFlush(customer)).thenReturn(Customer
                .builder()
                .id(CUSTOMER_ID)
                .title(CUSTOMER_TITLE)
                .name(CUSTOMER_NAME)
                .build());

        Optional<Customer> newCustomer = customerService.updateCustomer(CUSTOMER_ID, CUSTOMER_TITLE, CUSTOMER_NAME);

        verify(customerRepositoryMock, times(1)).findById(CUSTOMER_ID);
        verify(customer, times(1)).setTitle(CUSTOMER_TITLE);
        verify(customer, times(1)).setName(CUSTOMER_NAME);
        verify(customerRepositoryMock, times(1)).saveAndFlush(any());
        assertThat(newCustomer).isPresent();
    }

    @Test
    public void shouldCallFindAll_whenGetAllCustomers_givenNoCustomersExist() {
        List<Customer> customers = customerService.getAllCustomers();

        verify(customerRepositoryMock, times(1)).findAll();
        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    public void shouldCallFindAll_whenGetAllCustomers_givenCustomerExists() {
        when(customerRepositoryMock.findAll()).thenReturn(of(Customer
                .builder()
                .build()));

        List<Customer> customers = customerService.getAllCustomers();

        verify(customerRepositoryMock, times(1)).findAll();
        assertThat(customers.size()).isEqualTo(1);
    }

    @Test
    public void shouldCallFindById_whenGetCustomerById_givenNoCustomersWithIdExists() {
        Optional<Customer> customer = customerService.getCustomerById(CUSTOMER_ID);

        verify(customerRepositoryMock, times(1)).findById(CUSTOMER_ID);
        assertThat(customer).isEmpty();
    }

    @Test
    public void shouldCallFindById_whenGetCustomerById_givenCustomerWithIdExists() {
        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(Customer
                .builder()
                .id(CUSTOMER_ID)
                .build()));

        Optional<Customer> customer = customerService.getCustomerById(CUSTOMER_ID);

        verify(customerRepositoryMock, times(1)).findById(CUSTOMER_ID);
        assertThat(customer).isPresent();
    }

    @Test
    public void shouldCallFindCustomerByName_whenGetCustomerByName_givenNoCustomersWithIdExists() {
        Optional<Customer> customer = customerService.getCustomerByName(CUSTOMER_NAME);

        verify(customerRepositoryMock, times(1)).findCustomerByName(CUSTOMER_NAME);
        assertThat(customer).isEmpty();
    }

    @Test
    public void shouldCallFindCustomerByName_whenGetCustomerByName_givenCustomerWithIdExists() {
        when(customerRepositoryMock.findCustomerByName(CUSTOMER_NAME)).thenReturn(Optional.of(Customer
                .builder()
                .name(CUSTOMER_NAME)
                .build()));

        Optional<Customer> customer = customerService.getCustomerByName(CUSTOMER_NAME);

        verify(customerRepositoryMock, times(1)).findCustomerByName(CUSTOMER_NAME);
        assertThat(customer).isPresent();
    }

    @Test
    public void shouldCallFindById_whenGetBookingsByCustomerId_givenNoCustomersWithIdExists() {
        List<TourBooking> bookings = customerService.getBookingsByCustomerId(CUSTOMER_ID);

        verify(customerRepositoryMock, times(1)).findById(CUSTOMER_ID);
        assertThat(bookings).isNull();
    }

    @Test
    public void shouldCallFindById_whenGetBookingsByCustomerId_givenCustomerWithIdExists() {
        Customer customer = spy(Customer
                .builder()
                .id(CUSTOMER_ID)
                .bookings(of(TourBooking
                        .builder()
                        .build()))
                .build());
        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        List<TourBooking> bookings = customerService.getBookingsByCustomerId(CUSTOMER_ID);

        verify(customerRepositoryMock, times(1)).findById(CUSTOMER_ID);
        verify(customer, times(1)).getBookings();
        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void shouldNotDeleteAnyCustomer_whenDeleteCustomerById_givenNoCustomer() {
        Optional<Customer> customer = customerService.deleteCustomerById(CUSTOMER_ID);

        verify(customerRepositoryMock, times(1)).findById(CUSTOMER_ID);
        verify(customerRepositoryMock, times(0)).deleteById(CUSTOMER_ID);
        assertThat(customer).isEmpty();
    }

    @Test
    public void shouldDeleteACustomer_whenDeleteCustomerById_givenCustomerExists() {
        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(Customer
                .builder()
                .id(CUSTOMER_ID)
                .build()));

        Optional<Customer> customer = customerService.deleteCustomerById(CUSTOMER_ID);

        verify(customerRepositoryMock, times(1)).findById(CUSTOMER_ID);
        verify(customerRepositoryMock, times(1)).deleteById(CUSTOMER_ID);
        assertThat(customer).isPresent();
    }
}
