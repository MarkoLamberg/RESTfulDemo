package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TourBookingServiceIT extends AbstractRESTfulDemoIT {

    private static final int CUSTOMER_ID = 4;
    private static final int TOUR_ID = 1;
    private static final int PARTICIPANTS = 1;
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2020, 03, 20, 12, 00);
    private static final String LOCATION = "Hotel Ibis";

    @Autowired
    private TourBookingService tourBookingService;

    @Sql
    @Test
    public void shouldCreateABooking_whenCreateBooking_givenValidBooking() {
        List<TourBooking> bookingsBefore = tourBookingService.lookupAllBookings();
        assertThat(bookingsBefore.size()).isEqualTo(0);

        tourBookingService.createBooking(TOUR_ID, CUSTOMER_ID, DATE_TIME, LOCATION, PARTICIPANTS);

        List<TourBooking> bookingsAfter = tourBookingService.lookupAllBookings();
        TourBooking booking = bookingsAfter.get(0);

        assertThat(bookingsAfter.size()).isEqualTo(1);
        assertThat(booking.getTour().getId().intValue()).isEqualTo(TOUR_ID);
        assertThat(booking.getCustomer().getId().intValue()).isEqualTo(CUSTOMER_ID);
        assertThat(booking.getPickupLocation()).isEqualTo(LOCATION);
        assertThat(booking.getParticipants().intValue()).isEqualTo(PARTICIPANTS);
    }

    @Sql
    @Test
    public void shouldReturnBooking_whenLookupTourBookings_givenBookingWithTourIdExists() {
        List<TourBooking> bookings = tourBookingService.lookupTourBookings(TOUR_ID);
        assertThat(bookings.size()).isEqualTo(1);
        int tourId = bookings.get(0).getTour().getId();
        assertThat(bookings.get(0).getTour().getId().intValue()).isEqualTo(TOUR_ID);
    }

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenLookupAllBookings_givenBookingsExist() {
        List<TourBooking> bookings = tourBookingService.lookupAllBookings();
        assertThat(bookings.size()).isEqualTo(2);
    }

    @Sql
    @Test
    public void shouldUpdateBooking_whenUpdate_givenBookingWithTourIdAndCustomerIdExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsBefore.get(0).getPickupDateTime()).isNotEqualTo(DATE_TIME);
        assertThat(filteredBookingsBefore.get(0).getPickupLocation()).isNotEqualTo(LOCATION);

        tourBookingService.update(TOUR_ID, CUSTOMER_ID, DATE_TIME, LOCATION, PARTICIPANTS);

        List<TourBooking> filteredBookingsAfter = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsAfter.get(0).getPickupDateTime()).isEqualTo(DATE_TIME);
        assertThat(filteredBookingsAfter.get(0).getPickupLocation()).isEqualTo(LOCATION);
    }

    @Sql
    @Test
    public void shouldNotUpdateBooking_whenUpdate_givenMoreThanOneBookingsWithTourIdAndCustomerIdExist() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsBefore.size() == 1).isFalse();

        tourBookingService.update(TOUR_ID, CUSTOMER_ID, DATE_TIME, LOCATION, PARTICIPANTS);

        List<TourBooking> filteredBookingsAfter = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getPickupLocation().equals(LOCATION))
                .collect(Collectors.toList());

        assertThat(filteredBookingsAfter.size()).isEqualTo(0);
    }

    @Sql
    @Test
    public void shouldUpdateBooking_whenUpdateSome_givenBookingWithTourIdAndCustomerExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsBefore.get(0).getPickupDateTime()).isNotEqualTo(DATE_TIME);
        assertThat(filteredBookingsBefore.get(0).getPickupLocation()).isNotEqualTo(LOCATION);

        tourBookingService.update(TOUR_ID, CUSTOMER_ID, DATE_TIME, LOCATION, null);

        List<TourBooking> filteredBookingsAfter = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsAfter.get(0).getPickupDateTime()).isEqualTo(DATE_TIME);
        assertThat(filteredBookingsAfter.get(0).getPickupLocation()).isEqualTo(LOCATION);
        assertThat(filteredBookingsAfter.get(0).getPickupLocation()).isNotEqualTo(PARTICIPANTS);
    }

    @Sql
    @Test
    public void shouldDeleteBooking_whenDeleteAllBookingsWithTourId_givenBookingWithTourIdExists() {
        List<TourBooking> bookingsBefore = tourBookingService.lookupTourBookings(TOUR_ID);

        assertThat(bookingsBefore.size()).isEqualTo(1);

        List<TourBooking> deletedBookings = tourBookingService.deleteAllBookingsWithTourId(TOUR_ID);

        assertThat(deletedBookings.size()).isEqualTo(1);

        List<TourBooking> bookingsAfter = tourBookingService.lookupTourBookings(TOUR_ID);

        assertThat(bookingsAfter.size()).isEqualTo(0);
    }

    @Sql
    @Test
    public void shouldDeleteBooking_whenDeleteAllBookingsWithTourIdAndCustomerId_givenBookingWithTourIdAndCustomerIdExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsBefore.size()).isEqualTo(1);

        List<TourBooking> deletedBookings = tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);

        assertThat(deletedBookings.size()).isEqualTo(1);

        List<TourBooking> filteredBookingsAfter = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsAfter.size()).isEqualTo(0);
    }

    @Sql
    @Test
    public void shouldDeleteAllBookings_whenDeleteAllBookingsWithCustomerId_givenBookingsWithCustomerIdExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.lookupAllBookings()
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsBefore.size()).isEqualTo(2);

        List<TourBooking> deletedBookings = tourBookingService.deleteAllBookingsWithCustomerId(CUSTOMER_ID);

        assertThat(deletedBookings.size()).isEqualTo(2);

        List<TourBooking> filteredBookingsAfter = tourBookingService.lookupAllBookings()
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsAfter.size()).isEqualTo(0);
    }

    @Sql
    @Test
    public void shouldDeleteAllBookings_whenDeleteAllBookings_givenBookingsExists() {
        List<TourBooking> bookingsBefore = tourBookingService.lookupAllBookings();
        assertThat(bookingsBefore.size()).isEqualTo(3);

        List<TourBooking> deletedBookings = tourBookingService.deleteAllBookings();

        assertThat(deletedBookings.size()).isEqualTo(3);

        List<TourBooking> bookingsAfter = tourBookingService.lookupAllBookings();
        assertThat(bookingsAfter.size()).isEqualTo(0);
    }
}

