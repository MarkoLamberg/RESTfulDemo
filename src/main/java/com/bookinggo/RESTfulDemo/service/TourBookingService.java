package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.repository.TourBookingRepository;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class TourBookingService {
    private TourBookingRepository tourBookingRepository;
    private TourRepository tourRepository;

    public void createNew(int tourId, Integer customerId, String date, String location, Integer partisipants) throws NoSuchElementException {
        TourBooking tourBooking = new TourBooking(verifyTour(tourId), customerId,
                date, location, partisipants);
        tourBookingRepository.save(tourBooking);
    }

    public List<TourBooking> lookupBookings(int tourId) throws NoSuchElementException  {
        return tourBookingRepository.findByTourId(verifyTour(tourId).getId());
    }

    public List<TourBooking> lookupBookingsAfter(int tourId) throws NoSuchElementException {
        return tourBookingRepository.findAllByCustomerIdAfter (tourId);
    }

    public Iterable<TourBooking> lookupAll()  {
        return tourBookingRepository.findAll();
    }

    public TourBooking update(int tourId, Integer customerId, String date, String location) throws NoSuchElementException {
        TourBooking booking = verifyTourBooking(tourId, customerId);
        booking.setDate(date);
        booking.setPickupLocation(location);
        return tourBookingRepository.save(booking);
    }

    public TourBooking updateSome(int tourId, Integer customerId, String date, String location)
            throws NoSuchElementException {
        TourBooking booking = verifyTourBooking(tourId, customerId);

        if (date != null) {
            booking.setDate(date);
        }

        if (location!= null) {
            booking.setPickupLocation(location);
        }

        return tourBookingRepository.save(booking);
    }

    public void delete(int tourId, Integer customerId) throws NoSuchElementException {
        TourBooking booking = verifyTourBooking(tourId, customerId);
        tourBookingRepository.delete(booking);
    }

    public void delete(Integer customerId) throws NoSuchElementException {
        List<TourBooking> bookings = verifyTourBooking(customerId);

        for(TourBooking booking : bookings) {
            tourBookingRepository.delete(booking);
        }
    }

    public void deleteAll() throws NoSuchElementException {
        tourBookingRepository.deleteAll();
    }

    private Tour verifyTour(int tourId) throws NoSuchElementException {
        return tourRepository.findById(tourId).orElseThrow(() ->
                new NoSuchElementException("Tour does not exist " + tourId)
        );
    }

    private TourBooking verifyTourBooking(int tourId, int customerId) throws NoSuchElementException {
        return tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId).orElseThrow(() ->
                new NoSuchElementException("Tour-Booking pair for request "
                        + tourId + " for customer " + customerId));
    }

    private List<TourBooking> verifyTourBooking(int customerId) throws NoSuchElementException {
        return tourBookingRepository.findByCustomerId(customerId).orElseThrow(() ->
                new NoSuchElementException("Tour-Booking request for customer " + customerId));
    }
}
