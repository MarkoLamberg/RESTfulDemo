package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.service.TourBookingServiceImpl;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.TourServiceImpl;
import com.bookinggo.RESTfulDemo.web.BookingDto;
import com.bookinggo.RESTfulDemo.web.ExpandedBookingDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@RequestMapping(path = "/tours")
public class TourBookingController {
    @Autowired
    private TourBookingServiceImpl tourBookingServiceImpl;

    @Autowired
    private TourServiceImpl tourService;

    @PostMapping(path = "/{tourId}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourBooking(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        log.info("POST /tours/{}/bookings", tourId);
        tourBookingServiceImpl.createNew(tourId, bookingDto.getCustomerId(), bookingDto.getDate(),
                bookingDto.getPickupLocation(), bookingDto.getPartisipants());
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

        if(tour.isPresent()){
            return tour.get();
        }

        return null;
    }

    @GetMapping(path = "/{tourId}/bookings")
    public List<BookingDto> getAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        log.info("GET /tours/{}/bookings", tourId);
        List<TourBooking> tourBookings = tourBookingServiceImpl.lookupTourBookings(tourId);
        return tourBookings.stream().map(tourBooking -> toDto(tourBooking)).collect(Collectors.toList());
    }

    @GetMapping(path = "/bookings")
    public List<ExpandedBookingDto> getAllBookings() {
        log.info("GET /tours/bookings/");
        List<TourBooking> tourBookings = tourBookingServiceImpl.lookupAllBookings();
        return tourBookings.stream().map(tourBooking -> toExpandedDto(tourBooking)).collect(Collectors.toList());
    }

    @PutMapping(path = "/{tourId}/bookings")
    public BookingDto updateWithPut(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        log.info("PUT /tours/{}/bookings", tourId);
        return toDto(tourBookingServiceImpl.update(tourId, bookingDto.getCustomerId(),
                 bookingDto.getDate(), bookingDto.getPickupLocation(), bookingDto.getPartisipants()));
    }

    @PatchMapping(path = "/{tourId}/bookings")
    public BookingDto updateWithPatch(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        log.info("PATCH /tours/{}/bookings", tourId);
        return toDto(tourBookingServiceImpl.updateSome(tourId, bookingDto.getCustomerId(),
                 bookingDto.getDate(), bookingDto.getPickupLocation(), bookingDto.getPartisipants()));
    }

    @DeleteMapping("/{tourId}/bookings/{customerId}")
    public void delete(@PathVariable(value = "tourId") int tourId, @PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/{}/bookings", tourId);
        tourBookingServiceImpl.delete(tourId, customerId);
    }

    @DeleteMapping("/bookings/{customerId}")
    public void delete(@PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/bookings/{}", customerId);
        tourBookingServiceImpl.delete(customerId);
    }

    @DeleteMapping("/bookings")
    public void delete() {
        log.info("DELETE /tours/bookings/");
        tourBookingServiceImpl.deleteAll();
    }

    private BookingDto toDto(TourBooking tourBooking) {
        return new BookingDto(tourBooking.getDate(), tourBooking.getPickupLocation(), tourBooking.getCustomerId(),
                tourBooking.getPartisipants(), tourBooking.getTotalPriceString());
    }

    private ExpandedBookingDto toExpandedDto(TourBooking tourBooking) {
        return new ExpandedBookingDto(tourBooking.getDate(), tourBooking.getPickupLocation(), tourBooking.getCustomerId(),
                tourBooking.getPartisipants(), tourBooking.getTotalPriceString(), tourBooking.getTour().getId());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        log.info("NOT FOUND");
        return ex.getMessage();
    }
}
