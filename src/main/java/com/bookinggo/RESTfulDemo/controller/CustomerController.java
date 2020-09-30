package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.dto.CustomerDto;
import com.bookinggo.RESTfulDemo.dto.CustomerPatchDto;
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
        final Optional<Customer> customer = customerService.getCustomerByName(customerDto.getName());

        if (customer.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't create customer. Customer with that name already exists.");
        }

        final Customer createdCustomer = customerService.createCustomer(customerDto.getTitle(), customerDto.getName());

        return ResponseEntity
                .created(URI.create("/customers"))
                .body(createdCustomer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable(value = "customerId") int customerId, @Valid @RequestBody CustomerPatchDto customerPatchDto) {
        log.info("PUT /customers/{}, {}", customerId, customerPatchDto.toString());
        final Optional<Customer> customer = customerService.getCustomerById(customerId);

        if (customer.isPresent()) {
            final Optional<Customer> customerWithNewName = getCustomerWithNewName(customerPatchDto, customer);

            if (customerWithNewName.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't change the customer name to match with other existing customer.");
            } else {
                final Optional<Customer> response = customerService.updateCustomer(customerId, customerPatchDto.getTitle(), customerPatchDto.getName());

                if (response.isPresent()) {
                    return ResponseEntity
                            .ok()
                            .body(response.get());
                }
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't update customer. Customer doesn't exist. Provide correct Customer Id.");
    }

    private Optional<Customer> getCustomerWithNewName(@RequestBody @Valid CustomerPatchDto customerPatchDto, Optional<Customer> customer) {
        return ((customerPatchDto.getName() == null) || customer.get().getName().equals(customerPatchDto.getName())) ? Optional.empty() :
                customerService.getCustomerByName(customerPatchDto.getName());
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("GET /customers");
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomersById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customers/{}", customerId);
        final Optional<Customer> customer = customerService.getCustomerById(customerId);

        if (customer.isPresent()) {
            return ResponseEntity
                    .ok()
                    .body(customer.get());
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't get customer by id. Customer doesn't exist. Provide correct Customer Id.");
    }

    @GetMapping("/{customerId}/bookings")
    public ResponseEntity<?> getCustomersBookingsById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customers/{}/bookings", customerId);
        final Optional<Customer> customer = customerService.getCustomerById(customerId);

        if (customer.isPresent()) {
            final List<TourBooking> bookings = customerService.getBookingsByCustomerId(customerId);

            return ResponseEntity
                    .ok()
                    .body(bookings);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't get customer's bookings by id. Customer doesn't exist. Provide correct Customer Id.");
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /customers/{}", customerId);
        final Optional<Customer> customer = customerService.getCustomerById(customerId);

        if (customer.isPresent()) {
            final Optional<Customer> deletedCustomer = customerService.deleteCustomerById(customerId);

            return ResponseEntity
                    .ok()
                    .body(deletedCustomer.get());
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't delete customer. Customer doesn't exist. Provide correct Customer Id.");
    }
}
