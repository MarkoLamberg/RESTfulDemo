package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.repository.TourBookingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TourBookingServiceImpl implements TourBookingService {

    private final TourBookingRepository tourBookingRepository;
    private final TourService tourService;

    @Override
    public void createNew(int tourId, Integer customerId, String date, String location, Integer partisipants) throws NoSuchElementException {
        log.info("createNew - tourId: {}, customerId: {}, date: {}, location {}, partisipants {}", tourId, customerId, date, location, partisipants);

        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if(tour.isPresent()) {
            TourBooking tourBooking = new TourBooking(tour.get(), customerId,
                    date, location, partisipants);
            tourBookingRepository.save(tourBooking);
        }
    }

    @Override
    public List<TourBooking> lookupTourBookings(int tourId)  {
        log.info("lookupBookings - tourId: {}", tourId);
        return tourBookingRepository.findByTourId(tourId);
    }

    @Override
    public List<TourBooking> lookupAllBookings()  {
        return tourBookingRepository.findAll();
    }

    public TourBooking update(int tourId, Integer customerId, String date, String location, Integer partisipants) throws NoSuchElementException {
        log.info("update - tourId: {}, customerId: {}, date: {}, location {}", tourId, customerId, date, location);
        TourBooking booking = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);

        if(booking != null) {
            booking.setDate(date);
            booking.setPickupLocation(location);
            booking.setPartisipants(partisipants);
        }

        return tourBookingRepository.saveAndFlush(booking);
    }

    public TourBooking updateSome(int tourId, Integer customerId, String date, String location, Integer partisipants)
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

            if (partisipants != null) {
                booking.setPartisipants(partisipants);
            }
        }

        return tourBookingRepository.saveAndFlush(booking);
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
