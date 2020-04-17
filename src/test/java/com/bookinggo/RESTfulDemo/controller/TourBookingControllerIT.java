package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.util.RestfulDemoApplication;
import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.dto.BookingDto;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest(classes = RestfulDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integTest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TourBookingControllerIT{

    private static final int CUSTOMER_ID = 4;
    private static final int TOUR_ID = 1;
    private static final int PARTISIPANTS = 1;
    private static final String DATE = "20-03-2020";
    private static final String LOCATION = "Hotel Ibis";
    private static final String TOTALPRICE = "£250.00";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Sql
    @Test
    public void getAllTours_ToursExisting_ToursReturned(){
        Tour[] tours = this.restTemplate
                        .getForEntity("http://localhost:" + port + "/tours", Tour[].class)
                        .getBody();
        assertEquals(8, tours.length);
    }

    @Sql
    @Test
    public void getTourById_TourExists_TourReturned() {
        Tour tour = this.restTemplate
                .getForEntity("http://localhost:" + port + "/tours/" + TOUR_ID, Tour.class)
                .getBody();
        assertEquals(1, tour.getId().intValue());
    }

    @Sql
    @Test
    public void getBookingsForTourId_BookingsExist_BookingsReturned() {
        TourBooking[] tourBookings = this.restTemplate
                .getForEntity("http://localhost:" + port + "/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();
        assertEquals(2, tourBookings.length);
    }

    @Sql
    @Test
    public void getAllBookings_BookingsExist_BookingsReturned() {
        TourBooking[] tourBookings = this.restTemplate
                .getForEntity("http://localhost:" + port + "/tours/bookings", TourBooking[].class)
                .getBody();
        assertEquals(3, tourBookings.length);
    }

    @Sql
    @Test
    public void postABooking_ValidBooking_BookingCreated(){
        BookingDto bookingDto = new BookingDto(DATE, LOCATION, CUSTOMER_ID, PARTISIPANTS, TOTALPRICE);

        ResponseEntity<BookingDto> response = restTemplate.postForEntity("http://localhost:" + port + "/tours/" + TOUR_ID + "/bookings", bookingDto, BookingDto.class);

        assertEquals(201, response.getStatusCodeValue());
    }

    @Sql
    @Test
    public void updateBooking_ValidBooking_BookingUpdated(){
        BookingDto bookingDto = new BookingDto(DATE, LOCATION, CUSTOMER_ID, PARTISIPANTS, TOTALPRICE);

        HttpEntity<BookingDto> entity = new HttpEntity<BookingDto>(bookingDto);
        ResponseEntity<BookingDto> response = restTemplate.exchange("http://localhost:" + port + "/tours/" + TOUR_ID + "/bookings", HttpMethod.PUT, entity, BookingDto.class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(LOCATION, response.getBody().getPickupLocation());
        assertEquals(PARTISIPANTS, response.getBody().getPartisipants().intValue());
    }

    @Sql
    @Test
    public void deleteBookingsByTourIdAndCustomerId_bookingExists_BookingDeleted(){
        TourBooking[] tourBookingsBefore = this.restTemplate
                .getForEntity("http://localhost:" + port + "/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();

        List<TourBooking> filteredBookingsBefore = Arrays.stream(tourBookingsBefore)
                .filter(booking -> booking.getCustomerId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        restTemplate.delete("http://localhost:" + port + "/tours/" + TOUR_ID + "/bookings/" + CUSTOMER_ID);

        TourBooking[] tourBookingsAfter = this.restTemplate
                .getForEntity("http://localhost:" + port + "/tours/" + TOUR_ID + "/bookings", TourBooking[].class)
                .getBody();

        List<TourBooking> filteredBookingsAfter = Arrays.stream(tourBookingsAfter)
                .filter(booking -> booking.getCustomerId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertNotEquals(filteredBookingsBefore.size(), filteredBookingsAfter.size());
        assertEquals(0, filteredBookingsAfter.size());
    }

    @Sql
    @Test
    public void deleteBookingsByCustomerId_bookingsExist_BookingsDeleted(){
        TourBooking[] tourBookingsBefore = this.restTemplate
                .getForEntity("http://localhost:" + port + "/tours/bookings", TourBooking[].class)
                .getBody();

        List<TourBooking> filteredBookingsBefore = Arrays.stream(tourBookingsBefore)
                .filter(booking -> booking.getCustomerId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        restTemplate.delete("http://localhost:" + port + "/tours/bookings/" + CUSTOMER_ID);
        TourBooking[] tourBookingsAfter = this.restTemplate
                .getForEntity("http://localhost:" + port + "/tours/bookings", TourBooking[].class)
                .getBody();

        List<TourBooking> filteredBookingsAfter = Arrays.stream(tourBookingsAfter)
                .filter(booking -> booking.getCustomerId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertNotEquals(filteredBookingsBefore.size(), filteredBookingsAfter.size());
        assertEquals(0, filteredBookingsAfter.size());
    }

    @Sql
    @Test
    public void deleteAllBookings_bookingsExist_allBookingsDeleted(){
        TourBooking[] tourBookingsBefore = this.restTemplate
                .getForEntity("http://localhost:" + port + "/tours/bookings", TourBooking[].class)
                .getBody();


        restTemplate.delete("http://localhost:" + port + "/tours/bookings");
        TourBooking[] tourBookingsAfter = this.restTemplate
                .getForEntity("http://localhost:" + port + "/tours/bookings", TourBooking[].class)
                .getBody();


        assertNotEquals(tourBookingsBefore.length, tourBookingsAfter.length);
        assertEquals(0, tourBookingsAfter.length);
    }
}
