package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.dto.BookingDto;
import com.bookinggo.RESTfulDemo.dto.ExpandedBookingDto;
import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.TourBookingService;
import com.bookinggo.RESTfulDemo.service.TourService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/tours")
@Slf4j
@AllArgsConstructor
public class TourBookingController {

    private TourBookingService tourBookingService;
    private TourService tourService;

    @PostMapping(path = "/{tourId}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public TourBooking createTourBooking(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        log.info("POST /tours/{}/bookings", tourId);
        return tourBookingService.createNew(tourId, bookingDto.getCustomerId(), bookingDto.getDate(),
                bookingDto.getPickupLocation(), bookingDto.getParticipants());
    }

    @GetMapping
    public List<Tour> getAllTours() {
        log.info("GET /tours");
        return tourService.lookupAllTours();
    }

    @GetMapping(path = "/{tourId}")
    public Tour getToursById(@PathVariable(value = "tourId") int tourId) {
        log.info("GET /tours/{}", tourId);
        Optional<Tour> tour = tourService.lookupTourById(tourId);

        if (tour.isPresent()) {
            return tour.get();
        }

        return null;
    }

    @GetMapping(path = "/byLocation/{tourLocation}")
    public List<Tour> getToursByLocation(@PathVariable(value = "tourLocation") String location) {
        log.info("GET /tours/{}", location);
        List<Tour> tours = tourService.lookupToursByLocation(location);

        return tours;
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
    public BookingDto updateWithPut(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        log.info("PUT /tours/{}/bookings", tourId);
        return toDto(tourBookingService.update(tourId, bookingDto.getCustomerId(),
                bookingDto.getDate(), bookingDto.getPickupLocation(), bookingDto.getParticipants()));
    }

    @PatchMapping(path = "/{tourId}/bookings")
    public BookingDto updateWithPatch(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        log.info("PATCH /tours/{}/bookings", tourId);
        return toDto(tourBookingService.updateSome(tourId, bookingDto.getCustomerId(),
                bookingDto.getDate(), bookingDto.getPickupLocation(), bookingDto.getParticipants()));
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
        return new BookingDto(tourBooking.getDate(), tourBooking.getPickupLocation(), tourBooking.getCustomer().getId(),
                tourBooking.getParticipants(), tourBooking.getTotalPriceString());
    }

    private ExpandedBookingDto toExpandedDto(TourBooking tourBooking) {
        return new ExpandedBookingDto(tourBooking.getDate(), tourBooking.getPickupLocation(), tourBooking.getCustomer().getId(),
                tourBooking.getParticipants(), tourBooking.getTotalPriceString(), tourBooking.getTour().getId());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        log.info("NOT FOUND");
        return ex.getMessage();
    }
}
