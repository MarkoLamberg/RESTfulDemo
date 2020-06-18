package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;

import java.time.LocalDateTime;
import java.util.List;

public interface TourBookingService {

    public TourBooking createBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants);

    public List<TourBooking> lookupTourBookings(int tourId);

    public List<TourBooking> lookupAllBookings();

    public TourBooking updateBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants);

    public List<TourBooking> deleteAllBookingsWithTourId(int tourId);

    public List<TourBooking> deleteAllBookingsWithTourIdAndCustomerId(int tourId, Integer customerId);

    public List<TourBooking> deleteAllBookingsWithCustomerId(Integer customerId);

    public List<TourBooking> deleteAllBookings();

}
