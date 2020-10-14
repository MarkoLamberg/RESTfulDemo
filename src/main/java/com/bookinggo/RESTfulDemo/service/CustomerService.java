package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer createCustomer(String title, String name);

    Optional<Customer> updateCustomer(int customerId, String title, String name);

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(int customerId);

    Optional<Customer> getCustomerByName(String customerName);

    List<TourBooking> getBookingsByCustomerId(int customerId);

    Optional<Customer> deleteCustomerById(int customerId);
}
