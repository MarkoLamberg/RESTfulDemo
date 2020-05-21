package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/customers")
@Slf4j
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("GET /customers");
        return customerService.lookupAllCustomers();
    }

    @GetMapping(path = "/{customerId}")
    public Customer getCustomersById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customer/{}", customerId);
        Optional<Customer> customer = customerService.lookupCustomerById(customerId);

        if (customer.isPresent()) {
            return customer.get();
        }

        return null;
    }

    @GetMapping(path = "/{customerId}/bookings")
    public List<TourBooking> getCustomersBookingsById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customer/{}/bookings", customerId);
        List<TourBooking> bookings = customerService.lookupBookingsByCustomerId(customerId);

        return bookings;
    }
}
