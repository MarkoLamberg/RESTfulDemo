package com.bookinggo.RestfulDemo.controller;

import com.bookinggo.RestfulDemo.ControllerTests;
import com.bookinggo.RestfulDemo.dto.CustomerDto;
import com.bookinggo.RestfulDemo.dto.CustomerPatchDto;
import com.bookinggo.RestfulDemo.service.AbstractRestfulDemoIT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integratedTest")
class CustomerControllerITv2 extends AbstractRestfulDemoIT implements ControllerTests {

    @Autowired
    CustomerController customerController;

    private MockMvc mockMvc;

    private ObjectWriter objectWriter;

    private static final int CUSTOMER_ID = 1;
    private static final int NON_EXISTING_CUSTOMER_ID = 20;
    private static final String CUSTOMER_TITLE = "Mr";
    private static final String CUSTOMER_NAME = "Marko Lamberg";


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @Test
    public void shouldReturn201_whenCustomerCreated_givenValidCustomer() throws Exception {
        mockMvc.perform(post("/customers")
                .content(objectWriter.writeValueAsString(buildCustomerDto()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/CustomerControllerIT.shouldReturn400_whenCustomerCreated_givenCustomerWithThatNameAlreadyExists.sql")
    @Test
    public void shouldReturn400_whenCustomerCreated_givenCustomerWithThatNameAlreadyExists() throws Exception {
        mockMvc.perform(post("/customers")
                .content(objectWriter.writeValueAsString(buildCustomerDto()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/CustomerControllerIT.shouldReturn200_whenCustomerUpdated_givenValidCustomer.sql")
    @Test
    public void shouldReturn200_whenCustomerUpdated_givenValidCustomer() throws Exception {
        mockMvc.perform(put("/customers/" + CUSTOMER_ID)
                .content(objectWriter.writeValueAsString(CustomerPatchDto.builder()
                        .title(CUSTOMER_TITLE)
                        .name(CUSTOMER_NAME)
                        .build()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title").value(CUSTOMER_TITLE))
                .andExpect(jsonPath("$.name").value(CUSTOMER_NAME));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/CustomerControllerIT.shouldReturn200_whenCustomerUpdatedSome_givenValidCustomer.sql")
    @Test
    public void shouldReturn200_whenCustomerUpdatedSome_givenValidCustomer() throws Exception {
        mockMvc.perform(put("/customers/" + CUSTOMER_ID)
                .content(objectWriter.writeValueAsString(CustomerPatchDto.builder()
                        .title(CUSTOMER_TITLE)
                        .build()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title").value(CUSTOMER_TITLE))
                .andExpect(jsonPath("$.name").value(CUSTOMER_NAME));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/CustomerControllerIT.shouldReturn400_whenCustomerUpdated_givenCustomerWithIdDoesntExist.sql")
    @Test
    public void shouldReturn400_whenCustomerUpdated_givenCustomerWithIdDoesntExist() throws Exception {
        mockMvc.perform(put("/customers/" + NON_EXISTING_CUSTOMER_ID)
                .content(objectWriter.writeValueAsString(buildCustomerPatchDto()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/CustomerControllerIT.shouldReturn400_whenCustomerUpdated_givenCustomerWithNewNameExists.sql")
    @Test
    public void shouldReturn400_whenCustomerUpdated_givenCustomerWithNewNameExists() throws Exception {
        mockMvc.perform(put("/customers/" + CUSTOMER_ID)
                .content(objectWriter.writeValueAsString(buildCustomerPatchDto()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/CustomerControllerIT.shouldReturnFourCustomers_whenGetAllCustomers_givenCustomersExist.sql")
    @Test
    public void shouldReturnFourCustomers_whenGetAllCustomers_givenCustomersExist() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.[*].name")
                        .value(Matchers.containsInAnyOrder("Customer One", "Customer Two", "Customer Three", "Customer Four")));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/CustomerControllerIT.shouldReturnCustomer_whenGetCustomerById_givenCustomerExists.sql")
    @Test
    public void shouldReturnCustomer_whenGetCustomerById_givenCustomerExists() throws Exception {
        mockMvc.perform(get("/customers/" + CUSTOMER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(CUSTOMER_ID));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/CustomerControllerIT.shouldReturn400_whenGetCustomerById_givenCustomerDoesntExist.sql")
    @Test
    public void shouldReturn400_whenGetCustomerById_givenCustomerDoesntExist() throws Exception {
        mockMvc.perform(get("/customers/" + NON_EXISTING_CUSTOMER_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/CustomerControllerIT.shouldReturnTwoBookings_whenGetCustomersBookingsById_givenBookingsExist.sql")
    @Test
    public void shouldReturnTwoBookings_whenGetCustomersBookingsById_givenBookingsExist() throws Exception {
        mockMvc.perform(get("/customers/" + CUSTOMER_ID + "/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/CustomerControllerIT.shouldReturn400_whenGetCustomersBookingsById_givenCustomerDoesntExist.sql")
    @Test
    public void shouldReturn400_whenGetCustomersBookingsById_givenCustomerDoesntExist() throws Exception {
        mockMvc.perform(get("/customers/" + NON_EXISTING_CUSTOMER_ID + "/bookings"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/CustomerControllerIT.shouldDeleteCustomer_whenDeleteCustomer_givenCustomerExists.sql")
    @Test
    public void shouldDeleteCustomer_whenDeleteCustomer_givenCustomerExists() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(4)));

        mockMvc.perform(delete("/customers/" + CUSTOMER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(get("/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    public void shouldReturn400_whenDeleteCustomer_givenCustomerWithIdDoesntExist() throws Exception {
        mockMvc.perform(delete("/customers/" + NON_EXISTING_CUSTOMER_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private CustomerPatchDto buildCustomerPatchDto() {
        return CustomerPatchDto.builder()
                .name(CUSTOMER_NAME)
                .build();
    }

    private CustomerDto buildCustomerDto() {
        return CustomerDto.builder()
                .title(CUSTOMER_TITLE)
                .name(CUSTOMER_NAME)
                .build();
    }
}
