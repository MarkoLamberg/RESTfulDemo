package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.repository.TourBookingRepository;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TourBookingService {

    private TourBookingRepository tourBookingRepository;
    private TourRepository tourRepository;

    public void createNew(int tourId, Integer customerId, String date, String location, Integer partisipants) throws NoSuchElementException {
        log.info("createNew - tourId: {}, customerId: {}, date: {}, location {}, partisipants {}", tourId, customerId, date, location, partisipants);

        Optional<Tour> tours = tourRepository.findById(tourId);

        if(tours.isPresent()) {
            TourBooking tourBooking = new TourBooking(tours.get(), customerId,
                    date, location, partisipants);
            tourBookingRepository.save(tourBooking);
        }
    }

    public List<TourBooking> lookupBookings(int tourId)  {
        log.info("lookupBookings - tourId: {}", tourId);
        return tourBookingRepository.findByTourId(tourId);
    }

    public List<TourBooking> lookupBookingsAfter(int tourId) throws NoSuchElementException {
        log.info("lookupBookingsAfter - tourId: {}", tourId);
        return tourBookingRepository.findAllByCustomerIdAfter (tourId);
    }

    public List<TourBooking> lookupAllBookings()  {
        return tourBookingRepository.findAll();
    }

    public TourBooking update(int tourId, Integer customerId, String date, String location) throws NoSuchElementException {
        log.info("update - tourId: {}, customerId: {}, date: {}, location {}", tourId, customerId, date, location);
        TourBooking booking = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);

        if(booking != null) {
            booking.setDate(date);
            booking.setPickupLocation(location);
        }

        return tourBookingRepository.save(booking);
    }

    public TourBooking updateSome(int tourId, Integer customerId, String date, String location)
            throws NoSuchElementException {
        log.info("updateSome - tourId: {}, customerId: {}, date: {}, location {}", tourId, customerId, date, location);

        TourBooking booking = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);

        if(booking != null) {
            if (date != null) {
                booking.setDate(date);
            }

            if (location != null) {
                booking.setPickupLocation(location);
            }
        }

        return tourBookingRepository.save(booking);
    }

    public void delete(int tourId, Integer customerId) throws NoSuchElementException {
        log.info("delete - tourId: {}, customerId: {}", tourId, customerId);

        TourBooking booking = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);
        tourBookingRepository.delete(booking);
    }

    public void delete(Integer customerId) throws NoSuchElementException {
        log.info("delete - tourId: {}", customerId);

        List<TourBooking> bookings = tourBookingRepository.findByCustomerId(customerId);

        for(TourBooking booking : bookings) {
            tourBookingRepository.delete(booking);
        }
    }

    public void deleteAll() throws NoSuchElementException {
        log.info("deleteAll");

        tourBookingRepository.deleteAll();
    }
}
