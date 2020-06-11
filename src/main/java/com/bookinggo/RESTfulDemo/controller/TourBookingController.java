package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.dto.*;
import com.bookinggo.RESTfulDemo.entity.*;
import com.bookinggo.RESTfulDemo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/tours")
@Slf4j
@RequiredArgsConstructor
public class TourBookingController {

    private final TourBookingService tourBookingService;

    private final TourService tourService;

    private final CustomerService customerService;

    @PostMapping(path = "/{tourId}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public TourBooking createTourBooking(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody BookingDto bookingDto) {
        log.info("POST /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            LocalDateTime pickupDateTime = LocalDateTime.parse(bookingDto.getPickupDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            return tourBookingService.createNew(tourId, bookingDto.getCustomerId(), pickupDateTime,
                    bookingDto.getPickupLocation(), bookingDto.getParticipants());
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Tour Id");
    }

    @GetMapping(path = "/{tourId}/bookings")
    public List<BookingDto> getAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        log.info("GET /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            List<TourBooking> tourBookings = tourBookingService.lookupTourBookings(tourId);

            return tourBookings.stream().map(tourBooking -> toDto(tourBooking)).collect(Collectors.toList());
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Tour Id");
    }

    @GetMapping(path = "/bookings")
    public List<ExpandedBookingDto> getAllBookings() {
        log.info("GET /tours/bookings/");
        List<TourBooking> tourBookings = tourBookingService.lookupAllBookings();

        return tourBookings.stream().map(tourBooking -> toExpandedDto(tourBooking)).collect(Collectors.toList());
    }

    @PutMapping(path = "/{tourId}/bookings")
    public ResponseEntity<BookingDto> updateWithPut(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody BookingPatchDto bookingPatchDto) {
        log.info("PUT /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            LocalDateTime pickupDateTime = null;

            if (bookingPatchDto.getPickupDateTime() != null) {
                pickupDateTime = LocalDateTime.parse(bookingPatchDto.getPickupDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }

            TourBooking response = tourBookingService.update(tourId, bookingPatchDto.getCustomerId(),
                    pickupDateTime, bookingPatchDto.getPickupLocation(), bookingPatchDto.getParticipants());

            if (response == null) {
                return ResponseEntity.badRequest().body(null);
            }

            return ResponseEntity
                    .ok()
                    .body(toDto(response));
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Tour Id");
    }

    @DeleteMapping("/{tourId}/bookings")
    public List<BookingDto> deleteAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        log.info("DELETE /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithTourId(tourId);

            return listOfDtos(bookings);
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Tour Id");
    }

    @DeleteMapping("/{tourId}/bookings/{customerId}")
    public List<BookingDto> deleteAllBookingsForTourAndCustomer(@PathVariable(value = "tourId") int tourId, @PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/{}/bookings", tourId);
        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            Optional<Customer> customer = customerService.lookupCustomerById(customerId);

            if (customer.isPresent()) {
                List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(tourId, customerId);

                return listOfDtos(bookings);
            }

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Provide correct Customer Id");
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Tour Id");
    }

    @DeleteMapping("/bookings/{customerId}")
    public List<BookingDto> deleteAllBookingsForCustomer(@PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/bookings/{}", customerId);
        Optional<Customer> customer = customerService.lookupCustomerById(customerId);

        if (customer.isPresent()) {
            List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithCustomerId(customerId);

            return listOfDtos(bookings);
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Customer Id");
    }

    @DeleteMapping("/bookings")
    public List<BookingDto> deleteAllBookings() {
        log.info("DELETE /tours/bookings/");
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        log.info("NOT FOUND");
        return ex.getMessage();
    }
}
