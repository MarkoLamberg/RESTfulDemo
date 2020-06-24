package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.springframework.retry.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface TourBookingService {

    @Retryable(
            value = {SQLException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 1000))
    public TourBooking createBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) throws SQLException;

    @Recover
    public TourBooking recover(SQLException e, int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants);

    public List<TourBooking> getBookingsByTourId(int tourId);

    public List<TourBooking> getAllBookings();

    public TourBooking updateBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants);

    public List<TourBooking> deleteAllBookingsWithTourId(int tourId);

    public List<TourBooking> deleteAllBookingsWithTourIdAndCustomerId(int tourId, Integer customerId);

    public List<TourBooking> deleteAllBookingsWithCustomerId(Integer customerId);

    public List<TourBooking> deleteAllBookings();

}
