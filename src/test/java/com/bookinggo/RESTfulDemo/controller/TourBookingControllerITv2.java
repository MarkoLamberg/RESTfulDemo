package com.bookinggo.RestfulDemo.controller;

import com.bookinggo.RestfulDemo.ControllerTests;
import com.bookinggo.RestfulDemo.dto.BookingDto;
import com.bookinggo.RestfulDemo.dto.BookingPatchDto;
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
public class TourBookingControllerITv2 extends AbstractRestfulDemoIT implements ControllerTests {

    @Autowired
    TourBookingController tourBookingController;

    private MockMvc mockMvc;

    private ObjectWriter objectWriter;

    private static final int CUSTOMER_ID = 4;
    private static final int NON_EXISTING_CUSTOMER_ID = 123;
    private static final int TOUR_ID = 1;
    private static final int NON_EXISTING_TOUR_ID = 10;
    private static final int PARTICIPANTS = 1;
    private static final String PICKUP_DATE_TIME = "2020-03-20T12:00:00";
    private static final String PICKUP_LOCATION = "Hotel Ibis";
    private static final String TOTAL_PRICE = "£250.00";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tourBookingController).build();
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturn201_whenCreateBooking_givenValidBooking.sql")
    @Test
    public void shouldReturn201_whenCreateBooking_givenValidBooking() throws Exception {
        mockMvc.perform(post("/tours/" + TOUR_ID + "/bookings")
                .content(objectWriter.writeValueAsString(buildBookingDto()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturn400_whenCreateBooking_givenNotValidTourId.sql")
    @Test
    public void shouldReturn400_whenCreateBooking_givenNotValidTourId() throws Exception {
        mockMvc.perform(post("/tours/" + NON_EXISTING_TOUR_ID + "/bookings")
                .content(objectWriter.writeValueAsString(buildBookingDto()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturnTwoBookings_whenGetAllBookingsForTour_givenBookingsExist.sql")
    @Test
    public void shouldReturnTwoBookings_whenGetAllBookingsForTour_givenBookingsExist() throws Exception {
        mockMvc.perform(get("/tours/" + TOUR_ID + "/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturn400_whenGetAllBookingsForTour_givenNotValidTourId.sql")
    @Test
    public void shouldReturn400_whenGetAllBookingsForTour_givenNotValidTourId() throws Exception {
        mockMvc.perform(get("/tours/" + NON_EXISTING_TOUR_ID + "/bookings"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturnThreeBookings_whenGetTourBookings_givenBookingsExist.sql")
    @Test
    public void shouldReturnThreeBookings_whenGetTourBookings_givenBookingsExist() throws Exception {
        mockMvc.perform(get("/tours/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturn200_whenUpdateBooking_givenValidBooking.sql")
    @Test
    public void shouldReturn200_whenUpdateBooking_givenValidBooking() throws Exception {
        mockMvc.perform(put("/tours/" + TOUR_ID + "/bookings")
                .content(objectWriter.writeValueAsString(buildBookingPatchDto(PICKUP_DATE_TIME, PICKUP_LOCATION)))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.pickupLocation").value(PICKUP_LOCATION))
                .andExpect(jsonPath("$.participants").value(PARTICIPANTS));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturn200_whenUpdateBookingSome_givenValidBooking.sql")
    @Test
    public void shouldReturn200_whenUpdateBookingSome_givenValidBooking() throws Exception {
        mockMvc.perform(put("/tours/" + TOUR_ID + "/bookings")
                .content(objectWriter.writeValueAsString(buildBookingPatchDto(null, null)))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.pickupLocation").value("Hotel Intercontinental"))
                .andExpect(jsonPath("$.participants").value(PARTICIPANTS));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturn400_whenUpdateBooking_givenValidBookingButMoreThanOneBookingsWithSameCustomerAndSameLocation.sql")
    @Test
    public void shouldReturn400_whenUpdateBooking_givenValidBookingButMoreThanOneBookingsWithSameCustomerAndSameLocation() throws Exception {
        mockMvc.perform(put("/tours/" + TOUR_ID + "/bookings")
                .content(objectWriter.writeValueAsString(buildBookingDto()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturn400_whenUpdateBooking_givenNotValidTourId.sql")
    @Test
    public void shouldReturn400_whenUpdateBooking_givenNotValidTourId() throws Exception {
        mockMvc.perform(put("/tours/" + NON_EXISTING_TOUR_ID + "/bookings")
                .content(objectWriter.writeValueAsString(buildBookingDto()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldDeleteTwoBookings_whenDeleteAllBookingsForTour_givenBookingsExist.sql")
    @Test
    public void shouldDeleteTwoBookings_whenDeleteAllBookingsForTour_givenBookingsExist() throws Exception {
        mockMvc.perform(get("/tours/" + TOUR_ID + "/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));

        mockMvc.perform(delete("/tours/" + TOUR_ID + "/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(get("/tours/" + TOUR_ID + "/bookings"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturn400_whenDeleteAllBookingsForTour_givenNotValidTourId.sql")
    @Test
    public void shouldReturn400_whenDeleteAllBookingsForTour_givenNotValidTourId() throws Exception {
        mockMvc.perform(delete("/tours/" + NON_EXISTING_TOUR_ID + "/bookings"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldDeleteOneBooking_whenDeleteAllBookingsForTourAndCustomer_givenBookingExists.sql")
    @Test
    public void shouldDeleteOneBooking_whenDeleteAllBookingsForTourAndCustomer_givenBookingExists() throws Exception {
        mockMvc.perform(get("/tours/" + TOUR_ID + "/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));

        mockMvc.perform(delete("/tours/" + TOUR_ID + "/bookings/" + CUSTOMER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(get("/tours/" + TOUR_ID + "/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturn400_whenDeleteAllBookingsForTourAndCustomer_givenNotValidTourId.sql")
    @Test
    public void shouldReturn400_whenDeleteAllBookingsForTourAndCustomer_givenNotValidTourId() throws Exception {
        mockMvc.perform(delete("/tours/" + NON_EXISTING_TOUR_ID + "/bookings/" + CUSTOMER_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldDeleteTwoBookings_whenDeleteAllBookingsForCustomer_givenBookingsExist.sql")
    @Test
    public void shouldDeleteTwoBookings_whenDeleteAllBookingsForCustomer_givenBookingsExist() throws Exception {
        mockMvc.perform(get("/tours/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));

        mockMvc.perform(delete("/tours/bookings/" + CUSTOMER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(get("/tours/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturn400_whenDeleteAllBookingsForCustomer_givenNoCustomerExists.sql")
    @Test
    public void shouldReturn400_whenDeleteAllBookingsForCustomer_givenNoCustomerExists() throws Exception {
        mockMvc.perform(delete("/tours/bookings/" + NON_EXISTING_CUSTOMER_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldDeleteAllBookings_whenDeleteAllBookings_givenBookingsExist.sql")
    @Test
    public void shouldDeleteAllBookings_whenDeleteAllBookings_givenBookingsExist() throws Exception {
        mockMvc.perform(get("/tours/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));

        mockMvc.perform(delete("/tours/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(get("/tours/bookings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    private BookingDto buildBookingDto() {
        return BookingDto.builder()
                .pickupDateTime(PICKUP_DATE_TIME)
                .pickupLocation(PICKUP_LOCATION)
                .customerId(CUSTOMER_ID)
                .participants(PARTICIPANTS)
                .totalPrice(TOTAL_PRICE)
                .build();
    }

    private BookingPatchDto buildBookingPatchDto(String pickupDateTime, String pickupLocation) {
        return BookingPatchDto.builder()
                .pickupDateTime(pickupDateTime)
                .pickupLocation(pickupLocation)
                .customerId(CUSTOMER_ID)
                .participants(PARTICIPANTS)
                .totalPrice(TOTAL_PRICE)
                .build();
    }
}
