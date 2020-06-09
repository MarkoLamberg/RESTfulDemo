package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TourBookingRepositoryIT extends AbstractRepositoryIT {

    private static final int CUSTOMER_ID = 4;
    private static final int TOUR_ID = 1;

    @Autowired
    TourBookingRepository tourBookingRepository;

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenFindBookingsByTourId_givenBookingsExist() {
        List<TourBooking> bookings = tourBookingRepository.findByTourId(TOUR_ID);
        assertEquals(2, bookings.size());
    }

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenFindBookingsByCustomerId_givenBookingsExists() {
        List<TourBooking> bookings = tourBookingRepository.findByCustomerId(CUSTOMER_ID);
        assertEquals(2, bookings.size());
    }

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenFindBookingsByTourIdAndCustomerId_givenBookingsExists() {
        List<TourBooking> bookings = tourBookingRepository.findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        assertEquals(2, bookings.size());
    }
}
