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
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(classes = RestfulDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TourBookingControllerIT extends AbstractRESTfulDemoIT {

    private static final int CUSTOMER_ID = 4;

    private static final int TOUR_ID = 1;

    private static final int PARTICIPANTS = 1;

    private static final String LOCAL_HOST = "http://localhost:";

    private static final String DATE_TIME = "2020-03-20T12:00:00";

    private static final String LOCATION = "Hotel Ibis";

    private static final String TOTAL_PRICE = "£250.00";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TourBookingController tourBookingController;

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenGetBookingsForTourId_givenBookingsExist() {
        TourBooking[] tourBookings = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();
        assertEquals(2, tourBookings.length);
    }

    @Sql
    @Test
    public void shouldReturnThreeBookings_whenGetTourBookings_givenBookingsExist() {
        TourBooking[] tourBookings = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/bookings", TourBooking[].class)
                .getBody();
        assertEquals(3, tourBookings.length);
    }

    @Test
    public void shouldReturn201_whenBookingCreated_givenValidBooking() {
        BookingDto bookingDto = new BookingDto(DATE_TIME, LOCATION, CUSTOMER_ID, PARTICIPANTS, TOTAL_PRICE);

        ResponseEntity<BookingDto> response = restTemplate.postForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", bookingDto, BookingDto.class);

        assertEquals(CREATED.value(), response.getStatusCodeValue());
    }

    @Sql
    @Test
    public void shouldReturn200_whenBookingUpdated_givenValidBooking() {
        BookingDto bookingDto = new BookingDto(DATE_TIME, LOCATION, CUSTOMER_ID, PARTICIPANTS, TOTAL_PRICE);

        HttpEntity<BookingDto> entity = new HttpEntity<>(bookingDto);
        ResponseEntity<BookingDto> response = restTemplate.exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", HttpMethod.PUT, entity, BookingDto.class);

        assertEquals(OK.value(), response.getStatusCodeValue());
        assertEquals(LOCATION, response.getBody().getPickupLocation());
        assertEquals(PARTICIPANTS, response.getBody().getParticipants().intValue());
    }

    @Sql
    @Test
    public void shouldReturn400_whenBookingUpdated_givenValidBookingButMoreThanOneBookingsWithSameCustomerAndSameLocation() {
        BookingDto bookingDto = new BookingDto(DATE_TIME, LOCATION, CUSTOMER_ID, PARTICIPANTS, TOTAL_PRICE);

        HttpEntity<BookingDto> entity = new HttpEntity<>(bookingDto);
        ResponseEntity<BookingDto> response = restTemplate.exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", HttpMethod.PUT, entity, BookingDto.class);

        assertEquals(BAD_REQUEST.value(), response.getStatusCodeValue());
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
    public void shouldDeleteTwoBookingsByCustomerId_whenBookingsDeleted_givenBookingsExist() {
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
