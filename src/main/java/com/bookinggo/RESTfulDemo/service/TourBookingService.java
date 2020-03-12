package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import java.util.*;

public interface TourBookingService {
    public void createNew(int tourId, Integer customerId, String date, String location, Integer partisipants) throws NoSuchElementException;

    public List<TourBooking> lookupTourBookings(int tourId);

    public List<TourBooking> lookupAllBookings();
}
