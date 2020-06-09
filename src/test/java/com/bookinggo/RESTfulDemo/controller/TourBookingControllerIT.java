package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.RestfulDemoApplication;
import com.bookinggo.RESTfulDemo.dto.BookingDto;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.AbstractRESTfulDemoIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest(classes = RestfulDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TourBookingControllerIT extends AbstractRESTfulDemoIT {

    private static final int CUSTOMER_ID = 4;
    private static final int TOUR_ID = 1;
    private static final int PARTICIPANTS = 1;
    private static final String LOCAL_HOST = "http://localhost:";
    private static final String DATE = "20-03-2020";
    private static final String LOCATION = "Hotel Ibis";
    private static final String TOTAL_PRICE = "£250.00";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Sql
    @Test
    public void shouldReturnBookings_whenGetBookingsForTourId_givenBookingsExist() {
        TourBooking[] tourBookings = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();
        assertEquals(2, tourBookings.length);
    }

    @Sql
    @Test
    public void shouldReturnAllBookings_BookingsExist_BookingsReturned() {
        TourBooking[] tourBookings = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/bookings", TourBooking[].class)
                .getBody();
        assertEquals(3, tourBookings.length);
    }

    @Test
    public void shouldReturn201_whenBookingCreated_givenValidBooking() {
        BookingDto bookingDto = new BookingDto(DATE, LOCATION, CUSTOMER_ID, PARTICIPANTS, TOTAL_PRICE);

        ResponseEntity<BookingDto> response = restTemplate.postForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", bookingDto, BookingDto.class);

        assertEquals(201, response.getStatusCodeValue());
    }

    @Sql
    @Test
    public void shouldReturn200_whenBookingUpdated_givenValidBooking() {
        BookingDto bookingDto = new BookingDto(DATE, LOCATION, CUSTOMER_ID, PARTICIPANTS, TOTAL_PRICE);

        HttpEntity<BookingDto> entity = new HttpEntity<>(bookingDto);
        ResponseEntity<BookingDto> response = restTemplate.exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", HttpMethod.PUT, entity, BookingDto.class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(LOCATION, response.getBody().getPickupLocation());
        assertEquals(PARTICIPANTS, response.getBody().getParticipants().intValue());
    }

    @Sql
    @Test
    public void shouldDeleteBookingsByTourIdAndCustomerId_whenBookingDeleted_givenBookingExists() {
        TourBooking[] tourBookingsBefore = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();

        assertEquals(2, tourBookingsBefore.length);

        restTemplate.delete(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings/" + CUSTOMER_ID);

        TourBooking[] tourBookingsAfter = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();

        assertEquals(1, tourBookingsAfter.length);
    }

    @Sql
    @Test
    public void shouldDeleteBookingsByCustomerId_whenBookingsDeleted_givenBookingsExist() {
        TourBooking[] tourBookingsBefore = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/bookings", TourBooking[].class)
                .getBody();

        assertEquals(3, tourBookingsBefore.length);

        restTemplate.delete(LOCAL_HOST + port + "/tours/bookings/" + CUSTOMER_ID);

        TourBooking[] tourBookingsAfter = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/bookings", TourBooking[].class)
                .getBody();

        assertEquals(1, tourBookingsAfter.length);
    }

    @Sql
    @Test
    public void shouldDeleteAllBookings_whenAllBookingsDeleted_givenBookingsExist() {
        TourBooking[] tourBookingsBefore = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/bookings", TourBooking[].class)
                .getBody();


        restTemplate.delete(LOCAL_HOST + port + "/tours/bookings");
        TourBooking[] tourBookingsAfter = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/bookings", TourBooking[].class)
                .getBody();


        assertNotEquals(tourBookingsBefore.length, tourBookingsAfter.length);
        assertEquals(0, tourBookingsAfter.length);
    }
}
