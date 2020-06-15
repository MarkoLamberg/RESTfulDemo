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
    public List<Customer> lookupAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> lookupCustomerById(int id) {
        log.info("lookupCustomerById - customerId: {}", id);
        return customerRepository.findById(id);
    }

    @Override
    public List<TourBooking> lookupBookingsByCustomerId(int id) {
        log.info("lookupBookingsByCustomerId - customerId: {}", id);
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            return customer.get().getBookings();
        }

        return null;
    }
}
