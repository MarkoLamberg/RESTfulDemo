package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.service.TourBookingService;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.TourService;
import com.bookinggo.RESTfulDemo.web.BookingDto;
import com.bookinggo.RESTfulDemo.web.ExpandedBookingDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(path = "/tours")
public class TourBookingsController {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TourBookingsController.class);

    @Autowired
    private TourBookingService tourBookingService;

    @Autowired
    private TourService tourService;

    @PostMapping(path = "/{tourId}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourBooking(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        LOGGER.info("POST /tours/{}/bookings", tourId);
        tourBookingService.createNew(tourId, bookingDto.getCustomerId(), bookingDto.getDate(),
                bookingDto.getPickupLocation(), bookingDto.getPartisipants());
    }

    @GetMapping
    public List<Tour> getAllTours() {
        LOGGER.info("GET /tours");
        return tourService.lookupAllTours();
    }

    @GetMapping(path = "/{tourId}")
    public Tour getToursByPackage(@PathVariable(value = "tourId") int tourId) {
        LOGGER.info("GET /tours/{}", tourId);
        Optional<Tour> tour = tourService.lookupToursById(tourId);

        if(tour.isPresent()){
            return tour.get();
        }

        return null;
    }

    @GetMapping(path = "/{tourId}/bookings")
    public List<BookingDto> getAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        LOGGER.info("GET /tours/{}/bookings", tourId);
        List<TourBooking> tourBookings = tourBookingService.lookupBookings(tourId);
        return tourBookings.stream().map(tourBooking -> toDto(tourBooking)).collect(Collectors.toList());
    }

    @GetMapping(path = "/bookings")
    public List<ExpandedBookingDto> getAllBookings() {
        LOGGER.info("GET /tours/bookings/");
        List<TourBooking> tourBookings = tourBookingService.lookupBookingsAfter(0);
        return tourBookings.stream().map(tourBooking -> toExpandedDto(tourBooking)).collect(Collectors.toList());
    }

    @PutMapping(path = "/{tourId}/bookings")
    public BookingDto updateWithPut(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        LOGGER.info("PUT /tours/{}/bookings", tourId);
         return toDto(tourBookingService.update(tourId, bookingDto.getCustomerId(),
                 bookingDto.getDate(), bookingDto.getPickupLocation()));
    }

    @PatchMapping(path = "/{tourId}/bookings")
    public BookingDto updateWithPatch(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        LOGGER.info("PATCH /tours/{}/bookings", tourId);
        return toDto(tourBookingService.updateSome(tourId, bookingDto.getCustomerId(),
                 bookingDto.getDate(), bookingDto.getPickupLocation()));
    }

    @DeleteMapping("/{tourId}/bookings/{customerId}")
    public void delete(@PathVariable(value = "tourId") int tourId, @PathVariable(value = "customerId") int customerId) {
        LOGGER.info("DELETE /tours/{}/bookings", tourId);
        tourBookingService.delete(tourId, customerId);
    }

    @DeleteMapping("/bookings/{customerId}")
    public void delete(@PathVariable(value = "customerId") int customerId) {
        LOGGER.info("DELETE /tours/bookings/{}", customerId);
        tourBookingService.delete(customerId);
    }

    @DeleteMapping("/bookings")
    public void delete() {
        LOGGER.info("DELETE /tours/bookings/");
        tourBookingService.deleteAll();
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
        LOGGER.info("NOT FOUND");
        return ex.getMessage();
    }
}
