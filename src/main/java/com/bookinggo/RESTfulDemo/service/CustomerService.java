package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    public Customer createCustomer(String title, String name);

    public Optional<Customer> updateCustomer(int customerId, String title, String name);

    public List<Customer> getAllCustomers();

    public Optional<Customer> getCustomerById(int customerId);

    public Optional<Customer> getCustomerByName(String customerName);

    public List<TourBooking> getBookingsByCustomerId(int customerId);

    public Optional<Customer> deleteCustomerById(int customerId);
}
