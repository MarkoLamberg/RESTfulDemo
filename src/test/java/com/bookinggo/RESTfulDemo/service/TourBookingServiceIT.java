package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest
public class TourBookingServiceIT extends AbstractRESTfulDemoIT {

    private static final int CUSTOMER_ID = 4;
    private static final int TOUR_ID = 1;
    private static final int PARTICIPANTS = 1;
    private static final String DATE = "20-03-2020";
    private static final String LOCATION = "Hotel Ibis";

    @Autowired
    private TourBookingService tourBookingService;

    @Sql
    @Test
    public void shouldCreateABooking_whenCreateNew_givenValidBooking() {
        List<TourBooking> bookingsBefore = tourBookingService.lookupAllBookings();
        assertEquals(0, bookingsBefore.size());

        tourBookingService.createNew(TOUR_ID, CUSTOMER_ID, DATE, LOCATION, PARTICIPANTS);

        List<TourBooking> bookingsAfter = tourBookingService.lookupAllBookings();
        TourBooking booking = bookingsAfter.get(0);

        assertEquals(1, bookingsAfter.size());
        assertEquals(TOUR_ID, booking.getTour().getId().intValue());
        assertEquals(CUSTOMER_ID, booking.getCustomer().getId().intValue());
        assertEquals(LOCATION, booking.getPickupLocation());
        assertEquals(PARTICIPANTS, booking.getParticipants().intValue());
    }

    @Sql
    @Test
    public void shouldReturnBooking_whenLookupBooking_givenBookingWithTourIdExists() {
        List<TourBooking> bookings = tourBookingService.lookupTourBookings(TOUR_ID);
        assertEquals(1, bookings.size());
        int tourId = bookings.get(0).getTour().getId();
        assertEquals(TOUR_ID, bookings.get(0).getTour().getId().intValue());
    }

    @Sql
    @Test
    public void shouldReturnBookings_whenLookupAllBookings_givenBookingsExist() {
        List<TourBooking> bookings = tourBookingService.lookupAllBookings();
        assertEquals(2, bookings.size());
    }

    @Sql
    @Test
    public void shouldUpdateBooking_whenUpdate_givenBookingWithTourIdAndCustomerIdExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertNotEquals(DATE, filteredBookingsBefore.get(0).getDate());
        assertNotEquals(LOCATION, filteredBookingsBefore.get(0).getPickupLocation());

        tourBookingService.update(TOUR_ID, CUSTOMER_ID, DATE, LOCATION, PARTICIPANTS);

        List<TourBooking> filteredBookingsAfter = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertEquals(DATE, filteredBookingsAfter.get(0).getDate());
        assertEquals(LOCATION, filteredBookingsAfter.get(0).getPickupLocation());
    }

    @Sql
    @Test
    public void shouldUpdateBooking_whenUpdateSome_givenBookingWithTourIdAndCustomerExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertNotEquals(DATE, filteredBookingsBefore.get(0).getDate());
        assertNotEquals(LOCATION, filteredBookingsBefore.get(0).getPickupLocation());

        tourBookingService.updateSome(TOUR_ID, CUSTOMER_ID, DATE, LOCATION, PARTICIPANTS);

        List<TourBooking> filteredBookingsAfter = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertEquals(DATE, filteredBookingsAfter.get(0).getDate());
        assertEquals(LOCATION, filteredBookingsAfter.get(0).getPickupLocation());
    }

    @Sql
    @Test
    public void shouldDeleteBooking_whenDeleteAllBookingsWithTourIdAndCustomerId_givenBookingWithTourIdAndCustomerIdExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertEquals(1, filteredBookingsBefore.size());

        tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);

        List<TourBooking> filteredBookingsAfter = tourBookingService.lookupTourBookings(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertEquals(0, filteredBookingsAfter.size());
    }

    @Sql
    @Test
    public void shouldDeleteAllBookings_whenDeleteAllBookingsWithCustomerId_givenBookingsWithCustomerIdExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.lookupAllBookings()
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertEquals(2, filteredBookingsBefore.size());

        tourBookingService.deleteAllBookingsWithCustomerId(CUSTOMER_ID);

        List<TourBooking> filteredBookingsAfter = tourBookingService.lookupAllBookings()
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertEquals(0, filteredBookingsAfter.size());
    }

    @Sql
    @Test
    public void shouldDeleteAllBookings_whenDeleteAllBookings_givenBookingsExists() {
        List<TourBooking> bookingsBefore = tourBookingService.lookupAllBookings();
        assertEquals(3, bookingsBefore.size());

        tourBookingService.deleteAllBookings();

        List<TourBooking> bookingsAfter = tourBookingService.lookupAllBookings();
        assertEquals(0, bookingsAfter.size());
    }
}

