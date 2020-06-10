package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.dto.*;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.TourBookingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/tours")
@Slf4j
@AllArgsConstructor
public class TourBookingController {

    private final TourBookingService tourBookingService;

    @PostMapping(path = "/{tourId}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public TourBooking createTourBooking(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody BookingDto bookingDto) {
        log.info("POST /tours/{}/bookings", tourId);
        LocalDateTime pickupDateTime = LocalDateTime.parse(bookingDto.getPickupDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return tourBookingService.createNew(tourId, bookingDto.getCustomerId(), pickupDateTime,
                bookingDto.getPickupLocation(), bookingDto.getParticipants());
    }

    @GetMapping(path = "/{tourId}/bookings")
    public List<BookingDto> getAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        log.info("GET /tours/{}/bookings", tourId);
        List<TourBooking> tourBookings = tourBookingService.lookupTourBookings(tourId);
        return tourBookings.stream().map(tourBooking -> toDto(tourBooking)).collect(Collectors.toList());
    }

    @GetMapping(path = "/bookings")
    public List<ExpandedBookingDto> getAllBookings() {
        log.info("GET /tours/bookings/");
        List<TourBooking> tourBookings = tourBookingService.lookupAllBookings();
        return tourBookings.stream().map(tourBooking -> toExpandedDto(tourBooking)).collect(Collectors.toList());
    }

    @PutMapping(path = "/{tourId}/bookings")
    public ResponseEntity<BookingDto> updateWithPut(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody BookingDto bookingDto) {
        log.info("PUT /tours/{}/bookings", tourId);
        LocalDateTime pickupDateTime = LocalDateTime.parse(bookingDto.getPickupDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        TourBooking response = tourBookingService.update(tourId, bookingDto.getCustomerId(),
                pickupDateTime, bookingDto.getPickupLocation(), bookingDto.getParticipants());

        if (response == null) {
            return ResponseEntity.badRequest().body(bookingDto);
        }

        return ResponseEntity
                .ok()
                .body(toDto(response));
    }

    @PatchMapping(path = "/{tourId}/bookings")
    public ResponseEntity<BookingPatchDto> updateWithPatch(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody BookingPatchDto bookingPatchDto) {
        log.info("PATCH /tours/{}/bookings", tourId);
        LocalDateTime pickupDateTime = null;

        if (bookingPatchDto.getPickupDateTime() != null) {
            pickupDateTime = LocalDateTime.parse(bookingPatchDto.getPickupDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        TourBooking response = tourBookingService.updateSome(tourId, bookingPatchDto.getCustomerId(),
                pickupDateTime, bookingPatchDto.getPickupLocation(), bookingPatchDto.getParticipants());

        if (response == null) {
            return ResponseEntity.badRequest().body(bookingPatchDto);
        }

        return ResponseEntity
                .ok()
                .body(toPatchDto(response));
    }

    @DeleteMapping("/{tourId}/bookings/{customerId}")
    public void delete(@PathVariable(value = "tourId") int tourId, @PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/{}/bookings", tourId);
        tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(tourId, customerId);
    }

    @DeleteMapping("/bookings/{customerId}")
    public void delete(@PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/bookings/{}", customerId);
        tourBookingService.deleteAllBookingsWithCustomerId(customerId);
    }

    @DeleteMapping("/bookings")
    public void delete() {
        log.info("DELETE /tours/bookings/");
        tourBookingService.deleteAllBookings();
    }

    private BookingDto toDto(TourBooking tourBooking) {
        return new BookingDto(tourBooking.getPickupDateTime().toString(), tourBooking.getPickupLocation(), tourBooking.getCustomer().getId(),
                tourBooking.getParticipants(), tourBooking.getTotalPriceString());
    }

    private ExpandedBookingDto toExpandedDto(TourBooking tourBooking) {
        return new ExpandedBookingDto(tourBooking.getPickupDateTime().toString(), tourBooking.getPickupLocation(), tourBooking.getCustomer().getId(),
                tourBooking.getParticipants(), tourBooking.getTotalPriceString(), tourBooking.getTour().getId());
    }

    private BookingPatchDto toPatchDto(TourBooking tourBooking) {
        return new BookingPatchDto(tourBooking.getPickupDateTime().toString(), tourBooking.getPickupLocation(), tourBooking.getCustomer().getId(),
                tourBooking.getParticipants(), tourBooking.getTotalPriceString());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        log.info("NOT FOUND");
        return ex.getMessage();
    }
}
