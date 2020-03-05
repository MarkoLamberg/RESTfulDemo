package com.bookinggo.RESTfulDemo.Service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.repository.TourBookingRepository;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public void createNew(int tourId, Integer customerId, String date, String comment) throws NoSuchElementException {
        TourBooking tourBooking = new TourBooking(verifyTour(tourId), customerId,
                date, comment);
        tourBookingRepository.save(tourBooking);
    }

    public Page<TourBooking> lookupBookings(int tourId, Pageable pageable) throws NoSuchElementException  {
        return tourBookingRepository.findByTourId(verifyTour(tourId).getId(), pageable);
    }

    public Page<TourBooking> lookupBookingsAfter(int tourId, Pageable pageable) throws NoSuchElementException {
        return tourBookingRepository.findAllByCustomerIdAfter (tourId, pageable);
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
                new NoSuchElementException("Tour-Booking pair for request("
                        + tourId + " for customer" + customerId));
    }
}
