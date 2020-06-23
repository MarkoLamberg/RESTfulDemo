package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.*;
import com.bookinggo.RESTfulDemo.repository.CustomerRepository;
import com.bookinggo.RESTfulDemo.repository.TourBookingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class TourBookingServiceImpl implements TourBookingService {

    private final TourBookingRepository tourBookingRepository;

    private final TourService tourService;

    private final CustomerRepository customerRepository;

    @Override
    public TourBooking createBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) throws SQLException {
        log.info("createBooking - tourId: {}, customerId: {}, date: {}, location {}, participants {}", tourId, customerId, pickupDateTime, pickupLocation, participants);

        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            Optional<Customer> customer = customerRepository.findById(customerId);

            if (customer.isPresent()) {
                TourBooking tourBooking = TourBooking.builder()
                        .tour(tour.get())
                        .customer(customer.get())
                        .pickupDateTime(pickupDateTime)
                        .pickupLocation(pickupLocation)
                        .participants(participants)
                        .build();

                return tourBookingRepository.save(tourBooking);
            }
        }

        throw new SQLException("createBooking - failed - tour doesn't exist.");
    }

    @Override
    public TourBooking recover(SQLException e, int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) {
        log.info("Called createBooking - recover called instead: {}", e.getMessage());
        return null;
    }

    @Override
    public List<TourBooking> lookupTourBookings(int tourId) {
        log.info("lookupTourBookings - tourId: {}", tourId);
        return tourBookingRepository.findByTourId(tourId);
    }

    @Override
    public List<TourBooking> lookupAllBookings() {
        return tourBookingRepository.findAll();
    }

    @Override
    public TourBooking updateBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) {
        log.info("updateBooking - tourId: {}, customerId: {}, date: {}, location {}", tourId, customerId, pickupDateTime, pickupLocation);

        List<TourBooking> bookings = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);

        if (bookings.size() == 1) {
            if (pickupDateTime != null) {
                bookings.get(0).setPickupDateTime(pickupDateTime);
            }

            if (pickupLocation != null) {
                bookings.get(0).setPickupLocation(pickupLocation);
            }

            if (participants != null) {
                bookings.get(0).setParticipants(participants);
            }

            return tourBookingRepository.saveAndFlush(bookings.get(0));
        }

        return null;
    }

    @Override
    public List<TourBooking> deleteAllBookingsWithTourId(int tourId) {
        log.info("deleteAllBookingsWithTourId - tourId: {}", tourId);

        List<TourBooking> bookings = tourBookingRepository.findByTourId(tourId);

        bookings.forEach(booking -> tourBookingRepository.delete(booking));

        return bookings;
    }

    @Override
    public List<TourBooking> deleteAllBookingsWithTourIdAndCustomerId(int tourId, Integer customerId) {
        log.info("deleteAllBookingsWithTourIdAndCustomerId - tourId: {}, customerId: {}", tourId, customerId);

        List<TourBooking> bookings = tourBookingRepository.findByTourId(tourId)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList());

        bookings.forEach(booking -> tourBookingRepository.delete(booking));

        return bookings;
    }

    @Override
    public List<TourBooking> deleteAllBookingsWithCustomerId(Integer customerId) {
        log.info("deleteAllBookingsWithCustomerId - tourId: {}", customerId);

        List<TourBooking> bookings = tourBookingRepository.findByCustomerId(customerId);

        for (TourBooking booking : bookings) {
            tourBookingRepository.delete(booking);
        }

        return bookings;
    }

    @Override
    public List<TourBooking> deleteAllBookings() {
        log.info("deleteAllBookings");

        List<TourBooking> bookings = tourBookingRepository.findAll();

        tourBookingRepository.deleteAll();

        return bookings;
    }
}
