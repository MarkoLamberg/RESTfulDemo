package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.dto.CustomerDto;
import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/customers")
@Slf4j
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        log.info("POST /customers");
        Optional<Customer> customer = customerService.lookupCustomerByName(customerDto.getName());

        if (customer.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Customer with that name already exists");
        }

        return customerService.createCustomer(customerDto.getTitle(), customerDto.getName());
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("GET /customers");
        return customerService.lookupAllCustomers();
    }

    @GetMapping(path = "/{customerId}")
    public Customer getCustomersById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customers/{}", customerId);
        Optional<Customer> customer = customerService.lookupCustomerById(customerId);

        if (customer.isPresent()) {
            return customer.get();
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Customer Id");
    }

    @GetMapping(path = "/{customerId}/bookings")
    public List<TourBooking> getCustomersBookingsById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customers/{}/bookings", customerId);
        Optional<Customer> customer = customerService.lookupCustomerById(customerId);

        if (customer.isPresent()) {
            List<TourBooking> bookings = customerService.lookupBookingsByCustomerId(customerId);

            return bookings;
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Customer Id");
    }

    @DeleteMapping("/{customerId}")
    public Customer deleteCustomer(@PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /customers/{}", customerId);
        Optional<Customer> customer = customerService.lookupCustomerById(customerId);

        if (customer.isPresent()) {
            customerService.deleteCustomer(customerId);

            return customer.get();
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Customer Id");
    }
}
