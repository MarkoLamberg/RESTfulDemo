package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    public Customer createCustomer(String title, String name);

    public Customer updateCustomer(int customerId, String title, String name);

    public List<Customer> lookupAllCustomers();

    public Optional<Customer> lookupCustomerById(int customerId);

    public Optional<Customer> lookupCustomerByName(String customerName);

    public List<TourBooking> lookupBookingsByCustomerId(int customerId);

    public void deleteCustomer(int customerId);
}
