package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.dto.CustomerDto;
import com.bookinggo.RESTfulDemo.dto.CustomerPatchDto;
import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.exception.CustomerServiceException;
import com.bookinggo.RESTfulDemo.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
@Slf4j
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        log.info("POST /customers: {}", customerDto.toString());
        try {
            final Customer createdCustomer = customerService.createCustomer(customerDto.getTitle(), customerDto.getName());
            return ResponseEntity
                    .created(URI.create("/customers"))
                    .body(createdCustomer);
        } catch (CustomerServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable(value = "customerId") int customerId, @Valid @RequestBody CustomerPatchDto customerPatchDto) {
        log.info("PUT /customers/{}, {}", customerId, customerPatchDto.toString());
        try {
            final Customer customer = customerService.getCustomerById(customerId).get();
            final Optional<Customer> customerWithNewName = getCustomerWithNewName(customerPatchDto, customer);

            if (customerWithNewName.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't change the customer name to match with other existing customer.");
            } else {
                try {
                    final Optional<Customer> response = customerService.updateCustomer(customerId, customerPatchDto.getTitle(), customerPatchDto.getName());
                    return ResponseEntity
                            .ok()
                            .body(response.get());
                } catch (CustomerServiceException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
                }
            }
        } catch (CustomerServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("GET /customers");
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomersById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customers/{}", customerId);
        try {
            final Optional<Customer> customer = customerService.getCustomerById(customerId);
            return ResponseEntity
                    .ok()
                    .body(customer.get());
        } catch (CustomerServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{customerId}/bookings")
    public ResponseEntity<?> getCustomersBookingsById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customers/{}/bookings", customerId);
        try {
            final List<TourBooking> bookings = customerService.getBookingsByCustomerId(customerId);
            return ResponseEntity
                    .ok()
                    .body(bookings);
        } catch (CustomerServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /customers/{}", customerId);
        try {
            final Optional<Customer> deletedCustomer = customerService.deleteCustomerById(customerId);
            return ResponseEntity
                    .ok()
                    .body(deletedCustomer.get());
        } catch (CustomerServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private Optional<Customer> getCustomerWithNewName(@RequestBody @Valid CustomerPatchDto customerPatchDto, Customer customer) {
        if ((customerPatchDto.getName() == null) || customer.getName().equals(customerPatchDto.getName())) {
            return Optional.empty();
        } else {
            try {
                return customerService.getCustomerByName(customerPatchDto.getName());
            } catch (CustomerServiceException e) {
                return Optional.empty();
            }
        }
    }
}
