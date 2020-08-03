package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.springframework.retry.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TourBookingService {

    @Retryable(
            value = {SQLException.class},
            maxAttemptsExpression = "${retry.attempts:3}",
            backoff = @Backoff(delayExpression = "${retry.backoff.delay:500}"))
    public Optional<TourBooking> createBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) throws SQLException;

    @Recover
    public Optional<TourBooking> recover(SQLException e, int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants);

    public List<TourBooking> getBookingsByTourId(int tourId);

    public List<TourBooking> getAllBookings();

    public Optional<TourBooking> updateBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants);

    public List<TourBooking> deleteAllBookingsWithTourId(int tourId);

    public Optional<List<TourBooking>> deleteAllBookingsWithTourIdAndCustomerId(int tourId, Integer customerId);

    public Optional<List<TourBooking>> deleteAllBookingsWithCustomerId(Integer customerId);

    public List<TourBooking> deleteAllBookings();

}
