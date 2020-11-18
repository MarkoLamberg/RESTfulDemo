package com.bookinggo.RestfulDemo.service;

import com.bookinggo.RestfulDemo.entity.*;
import com.bookinggo.RestfulDemo.exception.TourBookingServiceException;
import com.bookinggo.RestfulDemo.exception.TourServiceException;
import com.bookinggo.RestfulDemo.repository.CustomerRepository;
import com.bookinggo.RestfulDemo.repository.TourBookingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public TourBooking createBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) throws TourBookingServiceException {
        log.info("createBooking - tourId: {}, customerId: {}, date: {}, location {}, participants {}", tourId, customerId, pickupDateTime, pickupLocation, participants);
        Tour tour;

        try {
            tour = tourService.getTourById(tourId);
        } catch (TourServiceException e) {
            throw new TourBookingServiceException(e.getMessage(), null);
        }

        final Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            final TourBooking tourBooking = TourBooking.builder()
                    .tour(tour)
                    .customer(customer.get())
                    .pickupDateTime(pickupDateTime)
                    .pickupLocation(pickupLocation)
                    .participants(participants)
                    .build();

            return tourBookingRepository.save(tourBooking);
        }

        throw new TourBookingServiceException("Can't create a booking for this Customer Id. Customer Id doesn't exist.", null);
    }

    @Override
    public TourBooking recover(TourBookingServiceException e) {
        log.info("Called createBooking - recover called instead: {}", e.getMessage());
        throw new TourBookingServiceException(e.getMessage(), null);
    }

    @Override
    public List<TourBooking> getBookingsByTourId(int tourId) {
        log.info("getBookingsByTourId - tourId: {}", tourId);

        List<TourBooking> tourBookings = tourBookingRepository.findByTourId(tourId);

        if (tourBookings.size() > 0) {
            return tourBookings;
        }

        throw new TourBookingServiceException("No Tour Bookings for given Tour Id.", null);
    }

    @Override
    public List<TourBooking> getAllBookings() {
        log.info("getAllBookings");
        return tourBookingRepository.findAll();
    }

    @Override
    public TourBooking updateBooking(int tourId, Integer customerId, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) {
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

                return tourBookingRepository.saveAndFlush(bookings.get(0));
            }

            throw new TourBookingServiceException("Can't update booking. More than one bookings with this Customer Id.", null);
        }

        throw new TourBookingServiceException("No Tour Bookings to update for given Customer Id.", null);
    }

    @Override
    public List<TourBooking> deleteAllBookingsWithTourId(int tourId) {
        log.info("deleteAllBookingsWithTourId - tourId: {}", tourId);
        final List<TourBooking> bookings = tourBookingRepository.findByTourId(tourId);

        bookings.forEach(tourBookingRepository::delete);

        return bookings;
    }

    @Override
    public List<TourBooking> deleteAllBookingsWithTourIdAndCustomerId(int tourId, Integer customerId) {
        log.info("deleteAllBookingsWithTourIdAndCustomerId - tourId: {}, customerId: {}", tourId, customerId);
        final List<TourBooking> bookings = tourBookingRepository.findByTourIdAndCustomerId(tourId, customerId);

        if (bookings.size() == 0) {
            throw new TourBookingServiceException("No Tour Bookings to delete for given Tour Id and Customer Id.", null);
        }

        bookings.forEach(booking -> {
            tourBookingRepository.delete(booking);
            tourBookingRepository.flush();
        });

        return bookings;
    }

    @Override
    @Transactional
    public List<TourBooking> deleteAllBookingsWithCustomerId(Integer customerId) {
        log.info("deleteAllBookingsWithCustomerId - tourId: {}", customerId);
        final List<TourBooking> bookings = tourBookingRepository.findByCustomerId(customerId);

        if (bookings.size() == 0) {
            throw new TourBookingServiceException("No Tour Bookings to delete for given Customer Id.", null);
        }

        for (TourBooking booking : bookings) {
            tourBookingRepository.delete(booking);
            tourBookingRepository.flush();
        }

        return bookings;
    }

    @Override
    public List<TourBooking> deleteAllBookings() {
        log.info("deleteAllBookings");
        final List<TourBooking> bookings = tourBookingRepository.findAll();

        if (bookings.size() == 0) {
            throw new TourBookingServiceException("No Tour Bookings to delete.", null);
        }

        tourBookingRepository.deleteAll();

        return bookings;
    }
}
