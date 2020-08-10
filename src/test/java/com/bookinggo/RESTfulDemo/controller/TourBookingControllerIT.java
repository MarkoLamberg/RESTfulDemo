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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(classes = RestfulDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TourBookingControllerIT extends AbstractRESTfulDemoIT {

    private static final int CUSTOMER_ID = 4;

    private static final int NON_EXISTING_CUSTOMER_ID = 123;

    private static final int TOUR_ID = 1;

    private static final int NON_EXISTING_TOUR_ID = 10;

    private static final int PARTICIPANTS = 1;

    private static final String LOCAL_HOST = "http://localhost:";

    private static final String PICKUP_DATE_TIME = "2020-03-20T12:00:00";

    private static final String PICKUP_LOCATION = "Hotel Ibis";

    private static final String TOTAL_PRICE = "£250.00";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Sql
    @Test
    public void shouldReturn201_whenCreateBooking_givenValidBooking() {
        ResponseEntity<BookingDto> response = restTemplate
                .postForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings",
                        buildBookingDto(),
                        BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(CREATED.value());
    }

    @Sql
    @Test
    public void shouldReturn400_whenCreateBooking_givenNotValidTourId() {
        ResponseEntity<BookingDto> response = restTemplate
                .postForEntity(LOCAL_HOST + port + "/tours/" + NON_EXISTING_TOUR_ID + "/bookings",
                        buildBookingDto(),
                        BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenGetAllBookingsForTour_givenBookingsExist() {
        TourBooking[] tourBookings = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();

        assertThat(tourBookings.length).isEqualTo(2);
    }

    @Sql
    @Test
    public void shouldReturn400_whenGetAllBookingsForTour_givenNotValidTourId() {
        ResponseEntity<String> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + NON_EXISTING_TOUR_ID + "/bookings",
                        HttpMethod.GET,
                        null,
                        String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturnThreeBookings_whenGetTourBookings_givenBookingsExist() {
        TourBooking[] tourBookings = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/bookings", TourBooking[].class)
                .getBody();

        assertThat(tourBookings.length).isEqualTo(3);
    }

    @Sql
    @Test
    public void shouldReturn200_whenUpdateBooking_givenValidBooking() {
        ResponseEntity<BookingDto> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings",
                        HttpMethod.PUT,
                        new HttpEntity<>(buildBookingPatchDto(PICKUP_DATE_TIME, PICKUP_LOCATION)),
                        BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(OK.value());
        assertThat(response.getBody().getPickupLocation()).isEqualTo(PICKUP_LOCATION);
        assertThat(response.getBody().getParticipants().intValue()).isEqualTo(PARTICIPANTS);
    }

    @Sql
    @Test
    public void shouldReturn200_whenUpdateBookingSome_givenValidBooking() {
        ResponseEntity<BookingDto> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings",
                        HttpMethod.PUT,
                        new HttpEntity<>(buildBookingPatchDto(null, null)), BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(OK.value());
        assertThat(response.getBody().getPickupLocation()).isNotEqualTo(PICKUP_LOCATION);
        assertThat(response.getBody().getParticipants().intValue()).isEqualTo(PARTICIPANTS);
    }

    @Sql
    @Test
    public void shouldReturn400_whenUpdateBooking_givenValidBookingButMoreThanOneBookingsWithSameCustomerAndSameLocation() {
        ResponseEntity<BookingDto> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID + "/bookings",
                        HttpMethod.PUT,
                        new HttpEntity<>(buildBookingDto()),
                        BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturn400_whenUpdateBooking_givenNotValidTourId() {
        ResponseEntity<BookingDto> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + NON_EXISTING_TOUR_ID + "/bookings",
                        HttpMethod.PUT,
                        new HttpEntity<>(buildBookingDto()),
                        BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldDeleteTwoBookings_whenDeleteAllBookingsForTour_givenBookingsExist() {
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
    public void shouldReturn400_whenDeleteAllBookingsForTour_givenNotValidTourId() {
        ResponseEntity<String> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + NON_EXISTING_TOUR_ID + "/bookings", HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldDeleteOneBooking_whenDeleteAllBookingsForTourAndCustomer_givenBookingExists() {
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
    public void shouldReturn400_whenDeleteAllBookingsForTourAndCustomer_givenNotValidTourId() {
        ResponseEntity<String> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + NON_EXISTING_TOUR_ID + "/bookings/" + CUSTOMER_ID, HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldDeleteTwoBookings_whenDeleteAllBookingsForCustomer_givenBookingsExist() {
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
    public void shouldReturn400_whenDeleteAllBookingsForCustomer_givenNoCustomerExists() {
        ResponseEntity<?> response = restTemplate.exchange(LOCAL_HOST + port + "/tours/bookings/" + NON_EXISTING_CUSTOMER_ID,
                HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldDeleteAllBookings_whenDeleteAllBookings_givenBookingsExist() {
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
