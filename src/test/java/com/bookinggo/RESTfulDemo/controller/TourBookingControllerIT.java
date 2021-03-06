package com.bookinggo.RestfulDemo.controller;

import com.bookinggo.RestfulDemo.ControllerTests;
import com.bookinggo.RestfulDemo.dto.*;
import com.bookinggo.RestfulDemo.entity.TourBooking;
import com.bookinggo.RestfulDemo.service.AbstractRestfulDemoIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TourBookingControllerIT extends AbstractRestfulDemoIT implements ControllerTests {

    private static final int CUSTOMER_ID = 4;
    private static final int BOOKING_ID = 3;
    private static final int NON_EXISTING_CUSTOMER_ID = 123;
    private static final int NON_EXISTING_BOOKING_ID = 5;
    private static final int TOUR_ID = 1;
    private static final int NON_EXISTING_TOUR_ID = 10;
    private static final int PARTICIPANTS = 1;
    private static final String PICKUP_DATE_TIME = "2020-03-20T12:00:00";
    private static final String PICKUP_LOCATION = "Hotel Ibis";
    private static final String TOTAL_PRICE = "£250.00";


    @Autowired
    private TestRestTemplate restTemplate;

    @Sql
    @Test
    public void shouldReturn201_whenCreateBooking_givenValidBooking() {
        ResponseEntity<BookingDto> response = restTemplate
                .postForEntity("/tours/" + TOUR_ID + "/bookings",
                        buildBookingDto(),
                        BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(CREATED.value());
    }

    @Sql
    @Test
    public void shouldReturn400_whenCreateBooking_givenNotValidTourId() {
        ResponseEntity<BookingDto> response = restTemplate
                .postForEntity("/tours/" + NON_EXISTING_TOUR_ID + "/bookings",
                        buildBookingDto(),
                        BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenGetAllBookingsForTour_givenBookingsExist() {
        TourBooking[] tourBookings = restTemplate
                .getForEntity("/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();

        assert tourBookings != null;
        assertThat(tourBookings.length).isEqualTo(2);
    }

    @Sql
    @Test
    public void shouldReturn400_whenGetAllBookingsForTour_givenNotValidTourId() {
        ResponseEntity<String> response = restTemplate
                .exchange("/tours/" + NON_EXISTING_TOUR_ID + "/bookings",
                        HttpMethod.GET, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturnTwoBookings_whenGetAllBookingsForTour_givenBookingsExist.sql")
    public void shouldReturnABooking_whenGetBookingById_givenBookingExists() {
        ExpandedBookingDto tourBooking = restTemplate
                .getForEntity("/tours/booking/" + BOOKING_ID, ExpandedBookingDto.class)
                .getBody();

        assertThat(tourBooking.getBookingId()).isEqualTo(BOOKING_ID);
    }

    @Test
    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturnTwoBookings_whenGetAllBookingsForTour_givenBookingsExist.sql")
    public void shouldReturn400_whenGetBookingById_givenBookingDoesntExist() {
        ResponseEntity<String> response = restTemplate
                .exchange("/tours/booking/" + NON_EXISTING_BOOKING_ID,
                        HttpMethod.GET, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturnThreeBookings_whenGetTourBookings_givenBookingsExist() {
        ExpandedBookingDto[] tourBookings = restTemplate
                .getForEntity("/tours/bookings", ExpandedBookingDto[].class)
                .getBody();

        assert tourBookings != null;
        assertThat(tourBookings.length).isEqualTo(3);
    }

    @Sql
    @Test
    public void shouldReturn200_whenUpdateBooking_givenValidBooking() {
        ResponseEntity<BookingDto> response = restTemplate
                .exchange("/tours/" + TOUR_ID + "/bookings",
                        HttpMethod.PUT,
                        new HttpEntity<>(buildBookingPatchDto(PICKUP_DATE_TIME, PICKUP_LOCATION)),
                        BookingDto.class);

        assertAll(
                () -> assertThat(response.getStatusCodeValue()).isEqualTo(OK.value()),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getPickupLocation()).isEqualTo(PICKUP_LOCATION),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getParticipants().intValue()).isEqualTo(PARTICIPANTS));
    }

    @Sql
    @Test
    public void shouldReturn200_whenUpdateBookingSome_givenValidBooking() {
        ResponseEntity<BookingDto> response = restTemplate
                .exchange("/tours/" + TOUR_ID + "/bookings",
                        HttpMethod.PUT,
                        new HttpEntity<>(buildBookingPatchDto(null, null)), BookingDto.class);

        assertAll(
                () -> assertThat(response.getStatusCodeValue()).isEqualTo(OK.value()),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getPickupLocation()).isNotEqualTo(PICKUP_LOCATION),
                () -> assertThat(Objects.requireNonNull(response.getBody()).getParticipants().intValue()).isEqualTo(PARTICIPANTS));
    }

    @Sql
    @Test
    public void shouldReturn400_whenUpdateBooking_givenValidBookingButMoreThanOneBookingsWithSameCustomerAndSameLocation() {
        ResponseEntity<BookingDto> response = restTemplate
                .exchange("/tours/" + TOUR_ID + "/bookings",
                        HttpMethod.PUT,
                        new HttpEntity<>(buildBookingDto()),
                        BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturn400_whenUpdateBooking_givenNotValidTourId() {
        ResponseEntity<BookingDto> response = restTemplate
                .exchange("/tours/" + NON_EXISTING_TOUR_ID + "/bookings",
                        HttpMethod.PUT,
                        new HttpEntity<>(buildBookingDto()),
                        BookingDto.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldDeleteTwoBookings_whenDeleteAllBookingsForTour_givenBookingsExist() {
        TourBooking[] tourBookingsBefore = restTemplate
                .getForEntity("/tours/" + TOUR_ID + "/bookings/", TourBooking[].class)
                .getBody();

        assert tourBookingsBefore != null;
        assertThat(tourBookingsBefore.length).isEqualTo(2);

        restTemplate.delete("/tours/" + TOUR_ID + "/bookings");

        ResponseEntity<String> response = restTemplate
                .exchange("/tours/" + TOUR_ID + "/bookings/", HttpMethod.GET, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturn400_whenDeleteAllBookingsForTour_givenNotValidTourId() {
        ResponseEntity<String> response = restTemplate
                .exchange("/tours/" + NON_EXISTING_TOUR_ID + "/bookings", HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldDeleteOneBooking_whenDeleteAllBookingsForTourAndCustomer_givenBookingExists() {
        TourBooking[] tourBookingsBefore = restTemplate
                .getForEntity("/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();

        assert tourBookingsBefore != null;
        assertThat(tourBookingsBefore.length).isEqualTo(2);

        restTemplate.delete("/tours/" + TOUR_ID + "/bookings/" + CUSTOMER_ID);

        TourBooking[] tourBookingsAfter = restTemplate
                .getForEntity("/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();

        assert tourBookingsAfter != null;
        assertThat(tourBookingsAfter.length).isEqualTo(1);
    }

    @Sql
    @Test
    public void shouldReturn400_whenDeleteAllBookingsForTourAndCustomer_givenNotValidTourId() {
        ResponseEntity<String> response = restTemplate
                .exchange("/tours/" + NON_EXISTING_TOUR_ID + "/bookings/" + CUSTOMER_ID, HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldDeleteTwoBookings_whenDeleteAllBookingsForCustomer_givenBookingsExist() {
        TourBooking[] tourBookingsBefore = restTemplate
                .getForEntity("/tours/bookings", TourBooking[].class)
                .getBody();

        assert tourBookingsBefore != null;
        assertThat(tourBookingsBefore.length).isEqualTo(3);

        restTemplate.delete("/tours/bookings/" + CUSTOMER_ID);

        TourBooking[] tourBookingsAfter = restTemplate
                .getForEntity("/tours/bookings", TourBooking[].class)
                .getBody();

        assert tourBookingsAfter != null;
        assertThat(tourBookingsAfter.length).isEqualTo(1);
    }

    @Sql
    @Test
    public void shouldReturn400_whenDeleteAllBookingsForCustomer_givenNoCustomerExists() {
        ResponseEntity<?> response = restTemplate.exchange("/tours/bookings/" + NON_EXISTING_CUSTOMER_ID,
                HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldDeleteAllBookings_whenDeleteAllBookings_givenBookingsExist() {
        TourBooking[] tourBookingsBefore = restTemplate
                .getForEntity("/tours/bookings", TourBooking[].class)
                .getBody();

        restTemplate.delete("/tours/bookings");

        ExpandedBookingDto[] tourBookingsAfter = restTemplate
                .getForEntity("/tours/bookings", ExpandedBookingDto[].class)
                .getBody();

        assertAll(
                () -> {
                    assert tourBookingsAfter != null;
                    assert tourBookingsBefore != null;
                    assertThat(tourBookingsAfter.length).isNotEqualTo(tourBookingsBefore.length);
                },
                () -> {
                    assert tourBookingsAfter != null;
                    assertThat(tourBookingsAfter.length).isEqualTo(0);
                });
    }

    @Test
    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturnTwoBookings_whenGetAllBookingsForTour_givenBookingsExist.sql")
    public void shouldDeleteABooking_whenDeleteBookingById_givenBookingsExist() {
        ResponseEntity<ExpandedBookingDto> tourBooking = restTemplate.exchange("/tours/booking/" + BOOKING_ID,
                HttpMethod.DELETE, null, ExpandedBookingDto.class);

        assertThat(tourBooking.getBody().getBookingId()).isEqualTo(BOOKING_ID);
    }

    @Test
    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourBookingControllerIT.shouldReturnTwoBookings_whenGetAllBookingsForTour_givenBookingsExist.sql")
    public void shouldReturn400_whenDeleteABooking_givenBookingExists() {
        ResponseEntity<?> response = restTemplate.exchange("/tours/booking/" + NON_EXISTING_BOOKING_ID,
                HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
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
