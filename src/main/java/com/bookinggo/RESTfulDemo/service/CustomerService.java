package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    public List<Customer> lookupAllCustomers();

    public Optional<Customer> lookupCustomerById(int id);

    public List<TourBooking> lookupBookingsByCustomerId(int id);

    public Customer createCustomer(String title, String name);
}
