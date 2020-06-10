package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public interface TourBookingService {

    public TourBooking createNew(int tourId, Integer customerId, LocalDateTime pickupDateTime, String location, Integer participants) throws NoSuchElementException;

    public List<TourBooking> lookupTourBookings(int tourId);

    public List<TourBooking> lookupAllBookings();

    public TourBooking update(int tourId, Integer customerId, LocalDateTime pickupDateTime, String location, Integer participants) throws NoSuchElementException;

    public void deleteAllBookingsWithTourIdAndCustomerId(int tourId, Integer customerId) throws NoSuchElementException;

    public void deleteAllBookingsWithCustomerId(Integer customerId) throws NoSuchElementException;

    public void deleteAllBookings() throws NoSuchElementException;

}
