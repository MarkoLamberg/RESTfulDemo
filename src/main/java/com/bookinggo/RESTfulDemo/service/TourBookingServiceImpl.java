package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
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

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class TourBookingServiceImpl implements TourBookingService {

    private final TourBookingRepository tourBookingRepository;

    private final TourService tourService;

    private final CustomerRepository customerRepository;

    @Override
    public Optional<TourBooking> createBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) throws SQLException {
        log.info("createBooking - tourId: {}, customerId: {}, date: {}, location {}, participants {}", tourId, customerId, pickupDateTime, pickupLocation, participants);
        final Optional<Tour> tour = tourService.getTourById(tourId);

        if (tour.isPresent()) {
            final Optional<Customer> customer = customerRepository.findById(customerId);

            if (customer.isPresent()) {
                final TourBooking tourBooking = TourBooking.builder()
                        .tour(tour.get())
                        .customer(customer.get())
                        .pickupDateTime(pickupDateTime)
                        .pickupLocation(pickupLocation)
                        .participants(participants)
                        .build();

                return Optional.of(tourBookingRepository.save(tourBooking));
            }

            return Optional.empty();
        }

        throw new SQLException("createBooking - failed - tour doesn't exist.");
    }

    @Override
    public Optional<TourBooking> recover(SQLException e, int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) {
        log.info("Called createBooking - recover called instead: {}", e.getMessage());
        return Optional.empty();
    }

    @Override
    public List<TourBooking> getBookingsByTourId(int tourId) {
        log.info("getBookingsByTourId - tourId: {}", tourId);
        return tourBookingRepository.findByTourId(tourId);
    }

    @Override
    public List<TourBooking> getAllBookings() {
        log.info("getAllBookings");
        return tourBookingRepository.findAll();
    }

    @Override
    public Optional<TourBooking> updateBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) {
        log.info("updateBooking - tourId: {}, customerId: {}, date: {}, location {}, participants {}", tourId, customerId, pickupDateTime, pickupLocation, participants);

        if (customerRepository.findById(customerId).isPresent()) {
            final List<TourBooking> bookings = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);

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

                return Optional.of(tourBookingRepository.saveAndFlush(bookings.get(0)));
            }
        }

        return Optional.empty();
    }

    @Override
    public List<TourBooking> deleteAllBookingsWithTourId(int tourId) {
        log.info("deleteAllBookingsWithTourId - tourId: {}", tourId);
        final List<TourBooking> bookings = tourBookingRepository.findByTourId(tourId);

        bookings.forEach(booking -> tourBookingRepository.delete(booking));

        return bookings;
    }

    @Override
    public Optional<List<TourBooking>> deleteAllBookingsWithTourIdAndCustomerId(int tourId, Integer customerId) {
        log.info("deleteAllBookingsWithTourIdAndCustomerId - tourId: {}, customerId: {}", tourId, customerId);
        final List<TourBooking> bookings = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);

        bookings.forEach(booking -> {
            tourBookingRepository.delete(booking);
            tourBookingRepository.flush();
        });

        final Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            return Optional.of(bookings);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<List<TourBooking>> deleteAllBookingsWithCustomerId(Integer customerId) {
        log.info("deleteAllBookingsWithCustomerId - tourId: {}", customerId);
        final List<TourBooking> bookings = tourBookingRepository.findByCustomerId(customerId);

        for (TourBooking booking : bookings) {
            tourBookingRepository.delete(booking);
            tourBookingRepository.flush();
        }

        final Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            return Optional.of(bookings);
        }

        return Optional.empty();
    }

    @Override
    public List<TourBooking> deleteAllBookings() {
        log.info("deleteAllBookings");
        final List<TourBooking> bookings = tourBookingRepository.findAll();

        if (bookings.size() > 0) {
            tourBookingRepository.deleteAll();
        }

        return bookings;
    }
}
