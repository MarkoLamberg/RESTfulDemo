package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.dto.*;
import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/customers")
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
                    BAD_REQUEST, "Customer with that name already exists.");
        }

        return customerService.createCustomer(customerDto.getTitle(), customerDto.getName());
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "customerId") int customerId, @Valid @RequestBody CustomerPatchDto customerPatchDto) {
        log.info("PUT /customers/{}", customerId);
        Optional<Customer> customer = customerService.lookupCustomerById(customerId);

        if (customer.isPresent()) {
            Optional<Customer> customerWithNewName = ((customerPatchDto.getName() == null) || customer.get().getName().equals(customerPatchDto.getName())) ? Optional.empty() :
                    customerService.lookupCustomerByName(customerPatchDto.getName());

            if (customerWithNewName.isPresent()) {
                throw new ResponseStatusException(
                        BAD_REQUEST, "Can't change the customer name to match with other existing customer.");
            } else {
                Customer response = customerService.updateCustomer(customerId, customerPatchDto.getTitle(), customerPatchDto.getName());

                if (response == null) {
                    return ResponseEntity.badRequest().body(null);
                }

                return ResponseEntity
                        .ok()
                        .body(response);
            }
        }

        throw new ResponseStatusException(
                BAD_REQUEST, "Customer doesn't exist. Provide correct Customer Id.");
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("GET /customers");
        return customerService.lookupAllCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomersById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customers/{}", customerId);
        Optional<Customer> customer = customerService.lookupCustomerById(customerId);

        if (customer.isPresent()) {
            return customer.get();
        }

        throw new ResponseStatusException(
                BAD_REQUEST, "Customer doesn't exist. Provide correct Customer Id.");
    }

    @GetMapping("/{customerId}/bookings")
    public ResponseEntity<?> getCustomersBookingsById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customers/{}/bookings", customerId);
        Optional<Customer> customer = customerService.lookupCustomerById(customerId);

        if (customer.isPresent()) {
            List<TourBooking> bookings = customerService.lookupBookingsByCustomerId(customerId);

            return ResponseEntity
                    .ok()
                    .body(bookings);
        }

        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.name().toLowerCase().replace('_', ' '))
                        .message("Customer doesn't exist. Provide correct Customer Id.")
                        .path("/customers/" + customerId + "/bookings")
                        .build());
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
                BAD_REQUEST, "Customer doesn't exist. Provide correct Customer Id.");
    }
}
