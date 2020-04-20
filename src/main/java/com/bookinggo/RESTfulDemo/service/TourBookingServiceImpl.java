package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.repository.TourBookingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class TourBookingServiceImpl implements TourBookingService {

    private final TourBookingRepository tourBookingRepository;
    private final TourService tourService;

    @Override
    public TourBooking createNew(int tourId, Integer customerId, String date, String location, Integer participants) throws NoSuchElementException {
        log.info("createNew - tourId: {}, customerId: {}, date: {}, location {}, participants {}", tourId, customerId, date, location, participants);

        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            TourBooking tourBooking = new TourBooking(tour.get(), customerId,
                    date, location, participants);
            tourBookingRepository.save(tourBooking);

            return tourBooking;
        }

        return null;
    }

    @Override
    public List<TourBooking> lookupTourBookings(int tourId) {
        log.info("lookupBookings - tourId: {}", tourId);
        return tourBookingRepository.findByTourId(tourId);
    }

    @Override
    public List<TourBooking> lookupAllBookings() {
        return tourBookingRepository.findAll();
    }

    @Override
    public TourBooking update(int tourId, Integer customerId, String date, String location, Integer participants) throws NoSuchElementException {
        log.info("update - tourId: {}, customerId: {}, date: {}, location {}", tourId, customerId, date, location);
        TourBooking booking = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);

        if (booking != null) {
            booking.setDate(date);
            booking.setPickupLocation(location);
            booking.setParticipants(participants);
        }

        return tourBookingRepository.saveAndFlush(booking);
    }

    @Override
    public TourBooking updateSome(int tourId, Integer customerId, String date, String location, Integer participants)
            throws NoSuchElementException {
        log.info("updateSome - tourId: {}, customerId: {}, date: {}, location {}", tourId, customerId, date, location);

        TourBooking booking = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);

        if (booking != null) {
            if (date != null) {
                booking.setDate(date);
            }

            if (location != null) {
                booking.setPickupLocation(location);
            }

            if (participants != null) {
                booking.setParticipants(participants);
            }
        }

        return tourBookingRepository.saveAndFlush(booking);
    }

    @Override
    public void delete(int tourId, Integer customerId) throws NoSuchElementException {
        log.info("delete - tourId: {}, customerId: {}", tourId, customerId);

        TourBooking booking = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);
        tourBookingRepository.delete(booking);
    }

    @Override
    public void delete(Integer customerId) throws NoSuchElementException {
        log.info("delete - tourId: {}", customerId);

        List<TourBooking> bookings = tourBookingRepository.findByCustomerId(customerId);

        for(TourBooking booking : bookings) {
            tourBookingRepository.delete(booking);
        }
    }

    @Override
    public void deleteAll() throws NoSuchElementException {
        log.info("deleteAll");

        tourBookingRepository.deleteAll();
    }
}
