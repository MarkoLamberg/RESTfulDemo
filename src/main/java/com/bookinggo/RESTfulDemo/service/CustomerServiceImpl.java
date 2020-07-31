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
    public Optional<Customer> updateCustomer(int customerId, String title, String name) {
        log.info("updateCustomer - customerId: {}, title: {}, name {}", customerId, title, name);

        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            if (title != null) {
                customer.get().setTitle(title);
            }

            if (name != null) {
                customer.get().setName(name);
            }

            return Optional.of(customerRepository.saveAndFlush(customer.get()));
        }

        return Optional.empty();
    }

    @Override
    public List<Customer> getAllCustomers() {
        log.info("getAllCustomers");
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(int customerId) {
        log.info("getCustomerById - customerId: {}", customerId);
        return customerRepository.findById(customerId);
    }

    @Override
    public Optional<Customer> getCustomerByName(String customerName) {
        log.info("getCustomerByName - customerName: {}", customerName);
        return customerRepository.findCustomerByName(customerName);
    }

    @Override
    public List<TourBooking> getBookingsByCustomerId(int customerId) {
        log.info("getBookingsByCustomerId - customerId: {}", customerId);
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            return customer.get().getBookings();
        }

        return null;
    }

    @Override
    public Optional<Customer> deleteCustomerById(int customerId) {
        log.info("deleteCustomerById - customerId: {}", customerId);

        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            customerRepository.deleteById(customerId);
            return customer;
        }

        return Optional.empty();
    }
}
