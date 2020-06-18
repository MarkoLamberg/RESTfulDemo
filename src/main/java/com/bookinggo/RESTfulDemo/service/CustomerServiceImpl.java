package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service

@Slf4j
@Transactional
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(String title, String name) {
        log.info("createCustomer - title: {}, name: {}", title, name);

        Customer customer = Customer.builder()
                .title(title)
                .name(name)
                .build();

        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(int customerId, String title, String name) {
        log.info("updateCustomer - customerId: {}, title: {}, name {}", customerId, title, name);

        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            if (title != null) {
                customer.get().setTitle(title);
            }

            if (name != null) {
                customer.get().setName(name);
            }

            return customerRepository.saveAndFlush(customer.get());
        }

        return null;
    }

    @Override
    public List<Customer> lookupAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> lookupCustomerById(int customerId) {
        log.info("lookupCustomerById - customerId: {}", customerId);
        return customerRepository.findById(customerId);
    }

    @Override
    public Optional<Customer> lookupCustomerByName(String customerName) {
        log.info("lookupCustomerByName - customerName: {}", customerName);
        return customerRepository.findCustomerByName(customerName);
    }

    @Override
    public List<TourBooking> lookupBookingsByCustomerId(int customerId) {
        log.info("lookupBookingsByCustomerId - customerId: {}", customerId);
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            return customer.get().getBookings();
        }

        return null;
    }

    @Override
    public void deleteCustomer(int customerId) {
        customerRepository.deleteById(customerId);
    }
}
