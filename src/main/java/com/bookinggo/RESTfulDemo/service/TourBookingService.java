package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;

import java.util.List;
import java.util.NoSuchElementException;

public interface TourBookingService {

    public TourBooking createNew(int tourId, Integer customerId, String date, String location, Integer participants) throws NoSuchElementException;

    public List<TourBooking> lookupTourBookings(int tourId);

    public List<TourBooking> lookupAllBookings();

    public TourBooking update(int tourId, Integer customerId, String date, String location, Integer participants) throws NoSuchElementException;

    public TourBooking updateSome(int tourId, Integer customerId, String date, String location, Integer participants);

    public void delete(int tourId, Integer customerId) throws NoSuchElementException;

    public void delete(Integer customerId) throws NoSuchElementException;

    public void deleteAll() throws NoSuchElementException;

}
