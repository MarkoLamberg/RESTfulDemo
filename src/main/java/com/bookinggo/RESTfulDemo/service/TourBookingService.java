package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;

import java.util.List;
import java.util.NoSuchElementException;

public interface TourBookingService {

    public TourBooking createNew(int tourId, Integer customerId, String date, String location, Integer partisipants) throws NoSuchElementException;

    public List<TourBooking> lookupTourBookings(int tourId);

    public List<TourBooking> lookupAllBookings();
}
