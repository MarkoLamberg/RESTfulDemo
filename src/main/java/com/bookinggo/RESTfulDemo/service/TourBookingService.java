package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.exception.TourBookingServiceException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TourBookingService {

    @Retryable(
            value = {TourBookingServiceException.class},
            maxAttemptsExpression = "${retry.attempts:3}",
            backoff = @Backoff(delayExpression = "${retry.backoff.delay:500}"))
    Optional<TourBooking> createBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) throws TourBookingServiceException;

    @Recover
    Optional<TourBooking> recover(TourBookingServiceException e);

    List<TourBooking> getBookingsByTourId(int tourId);

    List<TourBooking> getAllBookings();

    Optional<TourBooking> updateBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants);

    List<TourBooking> deleteAllBookingsWithTourId(int tourId);

    Optional<List<TourBooking>> deleteAllBookingsWithTourIdAndCustomerId(int tourId, Integer customerId);

    Optional<List<TourBooking>> deleteAllBookingsWithCustomerId(Integer customerId);

    List<TourBooking> deleteAllBookings();

}
