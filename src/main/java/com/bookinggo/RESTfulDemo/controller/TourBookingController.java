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
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/tours")
@Slf4j
@RequiredArgsConstructor
public class TourBookingController {

    private final TourBookingService tourBookingService;

    private final TourService tourService;

    @PostMapping("/{tourId}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public TourBooking createTourBooking(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody BookingDto bookingDto) throws SQLException {
        log.info("POST /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            LocalDateTime pickupDateTime = LocalDateTime.parse(bookingDto.getPickupDateTime(), ISO_LOCAL_DATE_TIME);

            return tourBookingService.createBooking(tourId, bookingDto.getCustomerId(), pickupDateTime,
                    bookingDto.getPickupLocation(), bookingDto.getParticipants());
        }

        throw new ResponseStatusException(
                BAD_REQUEST, "Tour doesn't exist. Provide correct Tour Id.");
    }

    @GetMapping("/{tourId}/bookings")
    public ResponseEntity<?> getAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        log.info("GET /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            List<TourBooking> tourBookings = tourBookingService.lookupTourBookings(tourId);

            return ResponseEntity
                    .ok()
                    .body(tourBookings
                            .stream()
                            .map(tourBooking -> toDto(tourBooking))
                            .collect(Collectors.toList()));
        }

        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.name().toLowerCase().replace('_', ' '))
                        .message("Tour doesn't exist. Provide correct Tour Id.")
                        .path("/tours/" + tourId + "/bookings")
                        .build());
    }

    @GetMapping("/bookings")
    public List<ExpandedBookingDto> getAllBookings() {
        log.info("GET /tours/bookings");
        List<TourBooking> tourBookings = tourBookingService.lookupAllBookings();

        return tourBookings.stream().map(tourBooking -> toExpandedDto(tourBooking)).collect(Collectors.toList());
    }

    @PutMapping("/{tourId}/bookings")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody BookingPatchDto bookingPatchDto) {
        log.info("PUT /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            LocalDateTime pickupDateTime = null;

            if (bookingPatchDto.getPickupDateTime() != null) {
                pickupDateTime = LocalDateTime.parse(bookingPatchDto.getPickupDateTime(), ISO_LOCAL_DATE_TIME);
            }

            TourBooking response = tourBookingService.updateBooking(tourId, bookingPatchDto.getCustomerId(),
                    pickupDateTime, bookingPatchDto.getPickupLocation(), bookingPatchDto.getParticipants());

            if (response == null) {
                return ResponseEntity.badRequest().body(null);
            }

            return ResponseEntity
                    .ok()
                    .body(toDto(response));
        }

        throw new ResponseStatusException(
                BAD_REQUEST, "Tour doesn't exist. Provide correct Tour Id.");
    }

    @DeleteMapping("/{tourId}/bookings")
    public ResponseEntity<?> deleteAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        log.info("DELETE /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithTourId(tourId);

            return ResponseEntity
                    .ok()
                    .body(listOfDtos(bookings));
        }

        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.name().toLowerCase().replace('_', ' '))
                        .message("Tour doesn't exist. Provide correct Tour Id.")
                        .path("/tours/" + tourId + "/bookings")
                        .build());
    }

    @DeleteMapping("/{tourId}/bookings/{customerId}")
    public ResponseEntity<?> deleteAllBookingsForTourAndCustomer(@PathVariable(value = "tourId") int tourId, @PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/{}/bookings/{}", tourId, customerId);
        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(tourId, customerId);

            return ResponseEntity
                    .ok()
                    .body(listOfDtos(bookings));
        }

        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.name().toLowerCase().replace('_', ' '))
                        .message("Tour doesn't exist. Provide correct Tour Id.")
                        .path("/tours/" + tourId + "/bookings/" + customerId)
                        .build());
    }

    @DeleteMapping("/bookings/{customerId}")
    public List<BookingDto> deleteAllBookingsForCustomer(@PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/bookings/{}", customerId);
        List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithCustomerId(customerId);

        return listOfDtos(bookings);
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
