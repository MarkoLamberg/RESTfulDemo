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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.bookinggo.RESTfulDemo.util.RestfulDemoUtil.badRequestResponse;

@RestController
@RequestMapping("/customers")
@Slf4j
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        log.info("POST /customers");
        Optional<Customer> customer = customerService.getCustomerByName(customerDto.getName());

        if (customer.isPresent()) {
            return badRequestResponse("Can't create customer. Customer with that name already exists.");
        }

        Customer createdCustomer = customerService.createCustomer(customerDto.getTitle(), customerDto.getName());

        return ResponseEntity
                .created(URI.create("/customers"))
                .body(createdCustomer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable(value = "customerId") int customerId, @Valid @RequestBody CustomerPatchDto customerPatchDto) {
        log.info("PUT /customers/{}", customerId);
        Optional<Customer> customer = customerService.getCustomerById(customerId);

        if (customer.isPresent()) {
            Optional<Customer> customerWithNewName = ((customerPatchDto.getName() == null) || customer.get().getName().equals(customerPatchDto.getName())) ? Optional.empty() :
                    customerService.getCustomerByName(customerPatchDto.getName());

            if (customerWithNewName.isPresent()) {
                return badRequestResponse("Can't change the customer name to match with other existing customer.");
            } else {
                Optional<Customer> response = customerService.updateCustomer(customerId, customerPatchDto.getTitle(), customerPatchDto.getName());

                if (response.isPresent()) {
                    return ResponseEntity
                            .ok()
                            .body(response.get());
                }

                return badRequestResponse("Can't update customer.");
            }
        }

        return badRequestResponse("Can't update customer. Customer doesn't exist. Provide correct Customer Id.");
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("GET /customers");
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomersById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customers/{}", customerId);
        Optional<Customer> customer = customerService.getCustomerById(customerId);

        if (customer.isPresent()) {
            return ResponseEntity
                    .ok()
                    .body(customer.get());
        }

        return badRequestResponse("Can't get customer by id. Customer doesn't exist. Provide correct Customer Id.");
    }

    @GetMapping("/{customerId}/bookings")
    public ResponseEntity<?> getCustomersBookingsById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customers/{}/bookings", customerId);
        Optional<Customer> customer = customerService.getCustomerById(customerId);

        if (customer.isPresent()) {
            List<TourBooking> bookings = customerService.getBookingsByCustomerId(customerId);

            return ResponseEntity
                    .ok()
                    .body(bookings);
        }

        return badRequestResponse("Can't get customer's bookings by id. Customer doesn't exist. Provide correct Customer Id.");
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /customers/{}", customerId);
        Optional<Customer> customer = customerService.getCustomerById(customerId);

        if (customer.isPresent()) {
            Optional<Customer> deletedCustomer = customerService.deleteCustomerById(customerId);

            return ResponseEntity
                    .ok()
                    .body(deletedCustomer.get());
        }

        return badRequestResponse("Can't delete customer. Customer doesn't exist. Provide correct Customer Id.");
    }
}
