package com.bookinggo.RestfulDemo.controller;

import com.bookinggo.RestfulDemo.dto.CustomerDto;
import com.bookinggo.RestfulDemo.dto.CustomerPatchDto;
import com.bookinggo.RestfulDemo.entity.Customer;
import com.bookinggo.RestfulDemo.entity.TourBooking;
import com.bookinggo.RestfulDemo.exception.CustomerServiceException;
import com.bookinggo.RestfulDemo.service.CustomerService;
import io.swagger.annotations.*;
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
@Api(tags = "Customer")
@Slf4j
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    //private final DynamoDBService dynamoDBService;

    @ApiOperation(value = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully creating a new customer", response = Customer.class),
            @ApiResponse(code = 400, message = "Failed creating a new customer")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
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

    @ApiOperation(value = "Update customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updating existing customer", response = Customer.class),
            @ApiResponse(code = 400, message = "Failed updating existing customer")
    })
    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "customerId") int customerId, @Valid @RequestBody CustomerPatchDto customerPatchDto) {
        log.info("PUT /customers/{}, {}", customerId, customerPatchDto.toString());
        try {
            final Customer customer = customerService.getCustomerById(customerId);
            final Optional<Customer> customerWithNewName = getCustomerWithNewName(customerPatchDto, customer);

            if (customerWithNewName.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't change the customer name to match with other existing customer.");
            } else {
                try {
                    final Customer response = customerService.updateCustomer(customerId, customerPatchDto.getTitle(), customerPatchDto.getName());
                    return ResponseEntity
                            .ok()
                            .body(response);
                } catch (CustomerServiceException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
                }
            }
        } catch (CustomerServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all customers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully getting all customers", response = Customer.class)
    })
    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("GET /customers");
        return customerService.getAllCustomers();
    }

    @ApiOperation(value = "Get customer by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully getting customer by id", response = Customer.class),
            @ApiResponse(code = 400, message = "Failed getting customer by id")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomersById(@PathVariable(value = "customerId") int customerId) {
        log.info("GET /customers/{}", customerId);
        try {
            final Customer customer = customerService.getCustomerById(customerId);
            return ResponseEntity
                    .ok()
                    .body(customer);
        } catch (CustomerServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get customer booking by customer id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully getting customer's bookings", response = TourBooking.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Failed getting customer's bookings")
    })
    @GetMapping("/{customerId}/bookings")
    public ResponseEntity<List<TourBooking>> getCustomersBookingsById(@PathVariable(value = "customerId") int customerId) {
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

    @ApiOperation(value = "Delete customer by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleting customer by id", response = Customer.class),
            @ApiResponse(code = 400, message = "Failed deleting customer by id")
    })
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /customers/{}", customerId);
        try {
            final Customer deletedCustomer = customerService.deleteCustomerById(customerId);
            return ResponseEntity
                    .ok()
                    .body(deletedCustomer);
        } catch (CustomerServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

   /*@ApiOperation(value = "Get DynamoDB dump for all customers created")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully getting dump for created customers", response = String.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Failed getting dump for created customers")
    })
    @GetMapping(path = "/dump")
    public ResponseEntity<List<String>> getCustomersDynamoDBTable() {
        log.info("GET /dump");
        try {
            final List<String> list = dynamoDBService.dumpTable(customerDynamoDBTableName);
            return ResponseEntity
                    .ok()
                    .body(list);
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }*/

    private Optional<Customer> getCustomerWithNewName(@RequestBody @Valid CustomerPatchDto customerPatchDto, Customer customer) {
        if ((customerPatchDto.getName() == null) || customer.getName().equals(customerPatchDto.getName())) {
            return Optional.empty();
        } else {
            try {
                return Optional.of(customerService.getCustomerByName(customerPatchDto.getName()));
            } catch (CustomerServiceException e) {
                return Optional.empty();
            }
        }
    }
}
