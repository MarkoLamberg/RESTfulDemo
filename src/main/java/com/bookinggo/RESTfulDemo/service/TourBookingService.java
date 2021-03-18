package com.bookinggo.RestfulDemo.service;

import com.bookinggo.RestfulDemo.entity.TourBooking;
import com.bookinggo.RestfulDemo.exception.TourBookingServiceException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.time.LocalDateTime;
import java.util.List;

public interface TourBookingService {

    @Retryable(
            value = {TourBookingServiceException.class},
            maxAttemptsExpression = "${retry.attempts:3}",
            backoff = @Backoff(delayExpression = "${retry.backoff.delay:500}"))
    TourBooking createBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) throws TourBookingServiceException;

    List<TourBooking> getBookingsByTourId(int tourId);

    List<TourBooking> getAllBookings();

    TourBooking updateBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants);

    List<TourBooking> deleteAllBookingsWithTourId(int tourId);

    List<TourBooking> deleteAllBookingsWithTourIdAndCustomerId(int tourId, Integer customerId);

    List<TourBooking> deleteAllBookingsWithCustomerId(Integer customerId);

    List<TourBooking> deleteAllBookings();

    TourBooking deleteBookingById(int bookingId);
}
