package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TourBookingRepositoryIT extends AbstractRepositoryIT {

    private static final int CUSTOMER_ID = 4;
    private static final int TOUR_ID = 1;

    @Autowired
    TourBookingRepository tourBookingRepository;

    @Sql
    @Test
    public void shouldReturnBookings_whenFindBookingsByTourId_givenBookingsExist() {
        List<TourBooking> bookings = tourBookingRepository.findByTourId(TOUR_ID);
        assertEquals(2, bookings.size());
    }

    @Sql
    @Test
    public void shouldReturnABooking_whenFindBookingByTourIdAndCustomerId_givenBookingExists() {
        TourBooking booking = tourBookingRepository.findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        assertNotNull(booking);
    }

    @Sql
    @Test
    public void shouldReturnBookings_whenFindBookingsByCustomerId_givenBookingsExists() {
        List<TourBooking> bookings = tourBookingRepository.findByCustomerId(CUSTOMER_ID);
        assertEquals(2, bookings.size());
    }
}
