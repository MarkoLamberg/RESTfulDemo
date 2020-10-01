package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.exception.CustomerServiceException;
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

        if (customerRepository.findCustomerByName(name).isPresent()) {
            throw new CustomerServiceException("Can't create customer. Customer with given name already exists.", null);
        }

        final Customer customer = Customer.builder()
                .title(title)
                .name(name)
                .build();

        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> updateCustomer(int customerId, String title, String name) {
        log.info("updateCustomer - customerId: {}, title: {}, name {}", customerId, title, name);
        final Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            if (title != null) {
                customer.get().setTitle(title);
            }

            if (name != null) {
                customer.get().setName(name);
            }

            return Optional.of(customerRepository.saveAndFlush(customer.get()));
        }

        throw new CustomerServiceException("Can't update customer. Customer doesn't exist. Provide correct Customer Id.", null);
    }

    @Override
    public List<Customer> getAllCustomers() {
        log.info("getAllCustomers");
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(int customerId) {
        log.info("getCustomerById - customerId: {}", customerId);
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            return customer;
        }

        throw new CustomerServiceException("Can't get customer by id. Customer doesn't exist. Provide correct Customer Id.", null);
    }

    @Override
    public Optional<Customer> getCustomerByName(String customerName) {
        log.info("getCustomerByName - customerName: {}", customerName);
        Optional<Customer> customer = customerRepository.findCustomerByName(customerName);

        if (customer.isPresent()) {
            return customer;
        }

        throw new CustomerServiceException("Can't get customer bookings by name. Customer doesn't exist. Provide correct Customer Id.", null);

    }

    @Override
    public List<TourBooking> getBookingsByCustomerId(int customerId) {
        log.info("getBookingsByCustomerId - customerId: {}", customerId);
        final Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            return customer.get().getBookings();
        }

        throw new CustomerServiceException("Can't get customer's bookings by id. Customer doesn't exist. Provide correct Customer Id.", null);
    }

    @Override
    public Optional<Customer> deleteCustomerById(int customerId) {
        log.info("deleteCustomerById - customerId: {}", customerId);
        final Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            customerRepository.deleteById(customerId);
            return customer;
        }

        throw new CustomerServiceException("Can't delete customer. Customer doesn't exist. Provide correct Customer Id.", null);
    }
}
