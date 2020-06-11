package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.RestfulDemoApplication;
import com.bookinggo.RESTfulDemo.dto.BookingDto;
import com.bookinggo.RESTfulDemo.dto.BookingPatchDto;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.AbstractRESTfulDemoIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
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

        assertThat(tourBookings.length).isEqualTo(2);
    }

    @Sql
    @Test
    public void shouldReturnThreeBookings_whenGetTourBookings_givenBookingsExist() {
        TourBooking[] tourBookings = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/bookings", TourBooking[].class)
                .getBody();

        assertThat(tourBookings.length).isEqualTo(3);
    }

    @Test
    public void shouldReturn201_whenBookingCreated_givenValidBooking() {
        BookingDto bookingDto = BookingDto.builder()
                .pickupDateTime(DATE_TIME)
                .pickupLocation(LOCATION)
                .customerId(CUSTOMER_ID)
                .participants(PARTICIPANTS)
                .totalPrice(TOTAL_PRICE)
                .build();

        ResponseEntity<BookingDto> response = restTemplate.postForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", bookingDto, BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(CREATED.value());
    }

    @Sql
    @Test
    public void shouldReturn200_whenBookingUpdated_givenValidBooking() {
        BookingPatchDto bookingPatchDto = BookingPatchDto.builder()
                .pickupDateTime(DATE_TIME)
                .pickupLocation(LOCATION)
                .customerId(CUSTOMER_ID)
                .participants(PARTICIPANTS)
                .totalPrice(TOTAL_PRICE)
                .build();
        HttpEntity<BookingPatchDto> entity = new HttpEntity<>(bookingPatchDto);
        ResponseEntity<BookingDto> response = restTemplate.exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", HttpMethod.PUT, entity, BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(OK.value());
        assertThat(response.getBody().getPickupLocation()).isEqualTo(LOCATION);
        assertThat(response.getBody().getParticipants().intValue()).isEqualTo(PARTICIPANTS);
    }

    @Sql
    @Test
    public void shouldReturn200_whenBookingUpdatedSome_givenValidBooking() {
        BookingPatchDto bookingPatchDto = BookingPatchDto.builder()
                .pickupDateTime(null)
                .pickupLocation(null)
                .customerId(CUSTOMER_ID)
                .participants(PARTICIPANTS)
                .totalPrice(TOTAL_PRICE)
                .build();
        HttpEntity<BookingPatchDto> entity = new HttpEntity<>(bookingPatchDto);
        ResponseEntity<BookingDto> response = restTemplate.exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", HttpMethod.PUT, entity, BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(OK.value());
        assertThat(response.getBody().getPickupLocation()).isNotEqualTo(LOCATION);
        assertThat(response.getBody().getParticipants().intValue()).isEqualTo(PARTICIPANTS);
    }

    @Sql
    @Test
    public void shouldReturn400_whenBookingUpdated_givenValidBookingButMoreThanOneBookingsWithSameCustomerAndSameLocation() {
        BookingDto bookingDto = BookingDto.builder()
                .pickupDateTime(DATE_TIME)
                .pickupLocation(LOCATION)
                .customerId(CUSTOMER_ID)
                .participants(PARTICIPANTS)
                .totalPrice(TOTAL_PRICE)
                .build();

        HttpEntity<BookingDto> entity = new HttpEntity<>(bookingDto);
        ResponseEntity<BookingDto> response = restTemplate.exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", HttpMethod.PUT, entity, BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldDeleteOneBookingByTourIdAndCustomerId_whenBookingDeleted_givenBookingExists() {
        TourBooking[] tourBookingsBefore = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();

        assertThat(tourBookingsBefore.length).isEqualTo(2);

        restTemplate.delete(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings/" + CUSTOMER_ID);

        TourBooking[] tourBookingsAfter = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();

        assertThat(tourBookingsAfter.length).isEqualTo(1);
    }

    @Sql
    @Test
    public void shouldDeleteTwoBookingsByCustomerId_whenBookingsDeleted_givenBookingsExist() {
        TourBooking[] tourBookingsBefore = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/bookings", TourBooking[].class)
                .getBody();

        assertThat(tourBookingsBefore.length).isEqualTo(3);

        restTemplate.delete(LOCAL_HOST + port + "/tours/bookings/" + CUSTOMER_ID);

        TourBooking[] tourBookingsAfter = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/bookings", TourBooking[].class)
                .getBody();

        assertThat(tourBookingsAfter.length).isEqualTo(1);
    }

    @Sql
    @Test
    public void shouldDeleteTwoBookingsByTourId_whenBookingsDeleted_givenBookingsExist() {
        TourBooking[] tourBookingsBefore = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings/", TourBooking[].class)
                .getBody();

        assertThat(tourBookingsBefore.length).isEqualTo(2);

        restTemplate.delete(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings");

        TourBooking[] tourBookingsAfter = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings/", TourBooking[].class)
                .getBody();

        assertThat(tourBookingsAfter.length).isEqualTo(0);
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


        assertThat(tourBookingsAfter.length).isNotEqualTo(tourBookingsBefore.length);
        assertThat(tourBookingsAfter.length).isEqualTo(0);
    }
}
