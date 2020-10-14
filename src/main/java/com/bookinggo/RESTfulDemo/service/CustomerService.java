package com.bookinggo.RestfulDemo.service;

import com.bookinggo.RestfulDemo.entity.Customer;
import com.bookinggo.RestfulDemo.entity.TourBooking;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(String title, String name);

    Customer updateCustomer(int customerId, String title, String name);

    List<Customer> getAllCustomers();

    Customer getCustomerById(int customerId);

    Customer getCustomerByName(String customerName);

    List<TourBooking> getBookingsByCustomerId(int customerId);

    Customer deleteCustomerById(int customerId);
}
