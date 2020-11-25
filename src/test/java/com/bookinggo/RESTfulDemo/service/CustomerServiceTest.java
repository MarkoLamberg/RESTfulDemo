package com.bookinggo.RestfulDemo.service;

import com.bookinggo.RestfulDemo.ServiceTests;
import com.bookinggo.RestfulDemo.entity.Customer;
import com.bookinggo.RestfulDemo.entity.TourBooking;
import com.bookinggo.RestfulDemo.exception.CustomerServiceException;
import com.bookinggo.RestfulDemo.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest implements ServiceTests {

    private static final int CUSTOMER_ID = 234;
    private static final String CUSTOMER_TITLE = "Mr";
    private static final String CUSTOMER_NAME = "Marko Lamberg";

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepositoryMock;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @Test
    public void shouldNotUpdateCustomer_whenUpdateCustomer_givenCustomerWithCustomerIdNonExisting() {
        assertAll(
                () -> assertThrows(CustomerServiceException.class,
                        () -> customerService.updateCustomer(CUSTOMER_ID, CUSTOMER_TITLE, CUSTOMER_NAME)),
                () -> verify(customerRepositoryMock).findById(CUSTOMER_ID));
    }

    @Test
    public void shouldUpdateACustomer_whenUpdateCustomerWithTwoFields_givenCustomerWithCustomerIdExists() {
        Customer customer = spy(buildCustomer());
        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(customerRepositoryMock.saveAndFlush(customerCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        Customer newCustomer = customerService.updateCustomer(CUSTOMER_ID, CUSTOMER_TITLE, CUSTOMER_NAME);

        assertAll(
                () -> verify(customerRepositoryMock).findById(CUSTOMER_ID),
                () -> verify(customer).setTitle(CUSTOMER_TITLE),
                () -> verify(customer).setName(CUSTOMER_NAME),
                () -> verify(customerRepositoryMock).saveAndFlush(any()),
                () -> assertThat(newCustomer).isNotNull(),
                () -> assertThat(newCustomer.getId()).isEqualTo(CUSTOMER_ID),
                () -> assertThat(newCustomer.getTitle()).isEqualTo(CUSTOMER_TITLE),
                () -> assertThat(newCustomer.getName()).isEqualTo(CUSTOMER_NAME));
    }

    @Test
    public void shouldUpdateACustomer_whenUpdateCustomerWithOneField_givenCustomerWithCustomerIdExists() {
        Customer customer = spy(buildCustomer());
        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(customerRepositoryMock.saveAndFlush(customerCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        Customer newCustomer = customerService.updateCustomer(CUSTOMER_ID, CUSTOMER_TITLE, null);

        assertAll(
                () -> verify(customerRepositoryMock).findById(CUSTOMER_ID),
                () -> verify(customer).setTitle(CUSTOMER_TITLE),
                () -> verify(customer, times(0)).setName(CUSTOMER_NAME),
                () -> verify(customerRepositoryMock).saveAndFlush(any()),
                () -> assertThat(newCustomer).isNotNull(),
                () -> assertThat(newCustomer.getId()).isEqualTo(CUSTOMER_ID),
                () -> assertThat(newCustomer.getTitle()).isEqualTo(CUSTOMER_TITLE));
    }

    @Test
    public void shouldNotUpdateACustomer_whenUpdateCustomerWithZeroFields_givenCustomerWithCustomerIdExists() {
        assertAll(
                () -> assertThrows(CustomerServiceException.class,
                        () -> customerService.updateCustomer(CUSTOMER_ID, null, null)),
                () -> verify(customerRepositoryMock).findById(CUSTOMER_ID));
    }

    @Test
    public void shouldCallFindAll_whenGetAllCustomers_givenNoCustomersExist() {
        List<Customer> customers = customerService.getAllCustomers();

        assertAll(
                () -> verify(customerRepositoryMock).findAll(),
                () -> assertThat(customers.size()).isEqualTo(0));
    }

    @Test
    public void shouldCallFindAll_whenGetAllCustomers_givenCustomerExists() {
        when(customerRepositoryMock.findAll()).thenReturn(List.of(buildCustomer()));
        List<Customer> customers = customerService.getAllCustomers();

        assertAll(
                () -> verify(customerRepositoryMock).findAll(),
                () -> assertThat(customers.size()).isEqualTo(1));
    }

    @Test
    public void shouldCallFindById_whenGetCustomerById_givenNoCustomersWithIdExists() {
        assertAll(
                () -> assertThrows(CustomerServiceException.class,
                        () -> customerService.getCustomerById(CUSTOMER_ID)),
                () -> verify(customerRepositoryMock).findById(CUSTOMER_ID));
    }

    @Test
    public void shouldCallFindById_whenGetCustomerById_givenCustomerWithIdExists() {
        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(buildCustomer()));
        Customer customer = customerService.getCustomerById(CUSTOMER_ID);

        assertAll(
                () -> verify(customerRepositoryMock).findById(CUSTOMER_ID),
                () -> assertThat(customer).isNotNull());
    }

    @Test
    public void shouldCallFindCustomerByName_whenGetCustomerByName_givenNoCustomersWithIdExists() {
        assertAll(
                () -> assertThrows(CustomerServiceException.class,
                        () -> customerService.getCustomerByName(CUSTOMER_NAME)),
                () -> verify(customerRepositoryMock).findCustomerByName(CUSTOMER_NAME));
    }

    @Test
    public void shouldCallFindCustomerByName_whenGetCustomerByName_givenCustomerWithIdExists() {
        when(customerRepositoryMock.findCustomerByName(CUSTOMER_NAME)).thenReturn(Optional.of(Customer
                .builder()
                .name(CUSTOMER_NAME)
                .build()));

        Customer customer = customerService.getCustomerByName(CUSTOMER_NAME);

        assertAll(
                () -> verify(customerRepositoryMock).findCustomerByName(CUSTOMER_NAME),
                () -> assertThat(customer).isNotNull());
    }

    @Test
    public void shouldCallFindById_whenGetBookingsByCustomerId_givenNoCustomersWithIdExists() {
        assertAll(
                () -> assertThrows(CustomerServiceException.class,
                        () -> customerService.getBookingsByCustomerId(CUSTOMER_ID)),
                () -> verify(customerRepositoryMock).findById(CUSTOMER_ID));
    }

    @Test
    public void shouldCallFindById_whenGetBookingsByCustomerId_givenCustomerWithIdExists() {
        Customer customer = spy(Customer
                .builder()
                .id(CUSTOMER_ID)
                .bookings(List.of(TourBooking
                        .builder()
                        .build()))
                .build());

        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        List<TourBooking> bookings = customerService.getBookingsByCustomerId(CUSTOMER_ID);

        assertAll(
                () -> verify(customerRepositoryMock).findById(CUSTOMER_ID),
                () -> verify(customer).getBookings(),
                () -> assertThat(bookings.size()).isEqualTo(1));
    }

    @Test
    public void shouldNotDeleteAnyCustomer_whenDeleteCustomerById_givenNoCustomer() {
        assertAll(
                () -> assertThrows(CustomerServiceException.class,
                        () -> customerService.deleteCustomerById(CUSTOMER_ID)),
                () -> verify(customerRepositoryMock).findById(CUSTOMER_ID),
                () -> verifyNoMoreInteractions(customerRepositoryMock));
    }

    @Test
    public void shouldDeleteACustomer_whenDeleteCustomerById_givenCustomerExists() {
        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(buildCustomer()));
        Customer customer = customerService.deleteCustomerById(CUSTOMER_ID);

        assertAll(
                () -> verify(customerRepositoryMock).findById(CUSTOMER_ID),
                () -> verify(customerRepositoryMock).deleteById(CUSTOMER_ID),
                () -> assertThat(customer).isNotNull());
    }

    private Customer buildCustomer() {
        return Customer
                .builder()
                .id(CUSTOMER_ID)
                .build();
    }
}
