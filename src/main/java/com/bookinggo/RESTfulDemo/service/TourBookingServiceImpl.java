package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.*;
import com.bookinggo.RESTfulDemo.repository.CustomerRepository;
import com.bookinggo.RESTfulDemo.repository.TourBookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TourBookingServiceImpl implements TourBookingService {

    private final TourBookingRepository tourBookingRepository;
    private final TourService tourService;
    private final CustomerRepository customerRepository;

    @Override
    public TourBooking createNew(int tourId, Integer customerId, LocalDateTime pickupDateTime, String location, Integer participants) throws NoSuchElementException {
        log.info("createNew - tourId: {}, customerId: {}, date: {}, location {}, participants {}", tourId, customerId, pickupDateTime, location, participants);

        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            Optional<Customer> customer = customerRepository.findById(customerId);

            if (customer.isPresent()) {
                TourBooking tourBooking = TourBooking.builder()
                        .tour(tour.get())
                        .customer(customer.get())
                        .pickupDateTime(pickupDateTime)
                        .pickupLocation(location)
                        .participants(participants)
                        .build();

                tourBookingRepository.save(tourBooking);

                return tourBooking;
            }
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
    public TourBooking update(int tourId, Integer customerId, LocalDateTime pickupDateTime, String location, Integer participants)
            throws NoSuchElementException {
        log.info("updateSome - tourId: {}, customerId: {}, date: {}, location {}", tourId, customerId, pickupDateTime, location);

        List<TourBooking> bookings = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);

        if (bookings.size() == 1) {
            if (pickupDateTime != null) {
                bookings.get(0).setPickupDateTime(pickupDateTime);
            }

            if (location != null) {
                bookings.get(0).setPickupLocation(location);
            }

            if (participants != null) {
                bookings.get(0).setParticipants(participants);
            }

            return tourBookingRepository.saveAndFlush(bookings.get(0));
        }

        return null;
    }

    @Override
    public List<TourBooking> deleteAllBookingsWithTourIdAndCustomerId(int tourId, Integer customerId) throws NoSuchElementException {
        log.info("delete - tourId: {}, customerId: {}", tourId, customerId);

        List<TourBooking> bookings = tourBookingRepository.findByTourId(tourId)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList());

        bookings.forEach(booking -> tourBookingRepository.delete(booking));

        return bookings;
    }

    @Override
    public List<TourBooking> deleteAllBookingsWithCustomerId(Integer customerId) throws NoSuchElementException {
        log.info("delete - tourId: {}", customerId);

        List<TourBooking> bookings = tourBookingRepository.findByCustomerId(customerId);

        for (TourBooking booking : bookings) {
            tourBookingRepository.delete(booking);
        }

        return bookings;
    }

    @Override
    public List<TourBooking> deleteAllBookings() throws NoSuchElementException {
        log.info("deleteAll");

        List<TourBooking> bookings = tourBookingRepository.findAll();

        tourBookingRepository.deleteAll();

        return bookings;
    }
}
