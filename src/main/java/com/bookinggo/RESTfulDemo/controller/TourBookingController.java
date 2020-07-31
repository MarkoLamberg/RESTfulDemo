package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.dto.*;
import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.TourBookingService;
import com.bookinggo.RESTfulDemo.service.TourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.bookinggo.RESTfulDemo.util.RestfulDemoUtil.badRequestResponse;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@RestController
@RequestMapping("/tours")
@Slf4j
@RequiredArgsConstructor
public class TourBookingController {

    private final TourBookingService tourBookingService;

    private final TourService tourService;

    @PostMapping("/{tourId}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createTourBooking(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody BookingDto bookingDto) throws SQLException {
        log.info("POST /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.getTourById(tourId);

        if (tour.isPresent()) {
            LocalDateTime pickupDateTime = LocalDateTime.parse(bookingDto.getPickupDateTime(), ISO_LOCAL_DATE_TIME);

            Optional<TourBooking> createdBooking = tourBookingService.createBooking(tourId, bookingDto.getCustomerId(), pickupDateTime,
                    bookingDto.getPickupLocation(), bookingDto.getParticipants());

            if (createdBooking.isPresent()) {
                return ResponseEntity
                        .created(URI.create("/tours/" + tourId + "/bookings"))
                        .body(toDto(createdBooking.get()));
            }

            return badRequestResponse("Can't create booking. Customer doesn't exist. Provide correct Customer Id.");
        }

        return badRequestResponse("Can't create booking. Tour doesn't exist. Provide correct Tour Id.");
    }

    @GetMapping("/{tourId}/bookings")
    public ResponseEntity<?> getAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        log.info("GET /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.getTourById(tourId);

        if (tour.isPresent()) {
            List<TourBooking> tourBookings = tourBookingService.getBookingsByTourId(tourId);

            return ResponseEntity
                    .ok()
                    .body(tourBookings
                            .stream()
                            .map(tourBooking -> toDto(tourBooking))
                            .collect(Collectors.toList()));
        }

        return badRequestResponse("Can't get bookings for tour. Tour doesn't exist. Provide correct Tour Id.");
    }

    @GetMapping("/bookings")
    public List<ExpandedBookingDto> getAllBookings() {
        log.info("GET /tours/bookings");
        List<TourBooking> tourBookings = tourBookingService.getAllBookings();

        return tourBookings.stream().map(tourBooking -> toExpandedDto(tourBooking)).collect(Collectors.toList());
    }

    @PutMapping("/{tourId}/bookings")
    public ResponseEntity<?> updateBooking(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody BookingPatchDto bookingPatchDto) {
        log.info("PUT /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.getTourById(tourId);

        if (tour.isPresent()) {
            LocalDateTime pickupDateTime = null;

            if (bookingPatchDto.getPickupDateTime() != null) {
                pickupDateTime = LocalDateTime.parse(bookingPatchDto.getPickupDateTime(), ISO_LOCAL_DATE_TIME);
            }

            Optional<TourBooking> response = tourBookingService.updateBooking(tourId, bookingPatchDto.getCustomerId(),
                    pickupDateTime, bookingPatchDto.getPickupLocation(), bookingPatchDto.getParticipants());

            if (response.isPresent()) {
                return ResponseEntity
                        .ok()
                        .body(toDto(response.get()));
            }

            return badRequestResponse("Can't update booking. No bookings for this customer id or more than one bookings for this customer id.");
        }

        return badRequestResponse("Can't update booking. Tour doesn't exist. Provide correct Tour Id.");
    }

    @DeleteMapping("/{tourId}/bookings")
    public ResponseEntity<?> deleteAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        log.info("DELETE /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.getTourById(tourId);

        if (tour.isPresent()) {
            List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithTourId(tourId);

            return ResponseEntity
                    .ok()
                    .body(listOfDtos(bookings));
        }

        return badRequestResponse("Can't delete bookings. Tour doesn't exist. Provide correct Tour Id.");
    }

    @DeleteMapping("/{tourId}/bookings/{customerId}")
    public ResponseEntity<?> deleteAllBookingsForTourAndCustomer(@PathVariable(value = "tourId") int tourId, @PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/{}/bookings/{}", tourId, customerId);
        Optional<Tour> tour = tourService.getTourById(tourId);

        if (tour.isPresent()) {
            Optional<List<TourBooking>> bookings = tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(tourId, customerId);

            if (bookings.isPresent()) {
                return ResponseEntity
                        .ok()
                        .body(listOfDtos(bookings.get()));
            }

            return badRequestResponse("Can't delete bookings. Customer doesn't exist. Provide correct Customer Id.");
        }

        return badRequestResponse("Can't delete bookings. Tour doesn't exist. Provide correct Tour Id.");
    }

    @DeleteMapping("/bookings/{customerId}")
    public ResponseEntity<?> deleteAllBookingsForCustomer(@PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/bookings/{}", customerId);

        Optional<List<TourBooking>> bookings = tourBookingService.deleteAllBookingsWithCustomerId(customerId);

        if (bookings.isPresent()) {
            return ResponseEntity
                    .ok()
                    .body(listOfDtos(bookings.get()));
        }

        return badRequestResponse("Can't delete customer bookings. Customer doesn't exist. Provide correct Customer Id.");
    }

    @DeleteMapping("/bookings")
    public List<BookingDto> deleteAllBookings() {
        log.info("DELETE /tours/bookings");
        List<TourBooking> bookings = tourBookingService.deleteAllBookings();

        return listOfDtos(bookings);
    }

    private BookingDto toDto(TourBooking tourBooking) {
        return BookingDto.builder()
                .pickupDateTime(tourBooking.getPickupDateTime().toString())
                .pickupLocation(tourBooking.getPickupLocation())
                .customerId(tourBooking.getCustomer().getId())
                .participants(tourBooking.getParticipants())
                .totalPrice(tourBooking.getTotalPriceString())
                .build();
    }

    private List<BookingDto> listOfDtos(List<TourBooking> bookings) {
        List<BookingDto> bookingDtos = new LinkedList<>();
        bookings.forEach(b -> bookingDtos.add(toDto(b)));

        return bookingDtos;
    }

    private ExpandedBookingDto toExpandedDto(TourBooking tourBooking) {
        return ExpandedBookingDto.childBuilder()
                .pickupDateTime(tourBooking.getPickupDateTime().toString())
                .pickupLocation(tourBooking.getPickupLocation())
                .customerId(tourBooking.getCustomer().getId())
                .participants(tourBooking.getParticipants())
                .totalPrice(tourBooking.getTotalPriceString())
                .tourId(tourBooking.getTour().getId())
                .build();
    }
}
