package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TourBookingRepositoryIT extends AbstractRepositoryIT {
    private static final int CUSTOMER_ID = 4;
    private static final int TOUR_ID = 1;

    @Autowired
    TourBookingRepository tourBookingRepository;

    @Test
    @Sql
    public void findBookingsByTourId_BookingsExist_ReturnBookings() {
        List<TourBooking> bookings = tourBookingRepository.findByTourId (TOUR_ID);
        assertEquals(2, bookings.size());
    }

    @Test
    @Sql
    public void findBookingByTourIdAndCustomerId_BookingExists_ReturnABooking() {
        TourBooking booking = tourBookingRepository.findByTourIdAndCustomerId (TOUR_ID, CUSTOMER_ID);
        assertNotNull(booking);
    }

    @Test
    @Sql
    public void findBookingsByCustomerId_BookingsExists_ReturnBookings() {
        List<TourBooking> bookings = tourBookingRepository.findByCustomerId (CUSTOMER_ID);
        assertEquals(2, bookings.size());
    }
}
