package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.service.TourBookingService;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.web.BookingDto;
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
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(path = "/tours/{tourId}/bookings")
public class TourBookingsController {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TourBookingsController.class);

    @Autowired
    private TourBookingService tourBookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourBooking(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        LOGGER.info("POST /tours/{}/bookings", tourId);
        tourBookingService.createNew(tourId, bookingDto.getCustomerId(), bookingDto.getDate(),
                bookingDto.getPickupLocation(), bookingDto.getPartisipants());
    }

    @GetMapping
    public List<BookingDto> getAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        LOGGER.info("GET /tours/{}/bookings", tourId);
        List<TourBooking> tourBookings = tourBookingService.lookupBookings(tourId);
        return tourBookings.stream().map(tourBooking -> toDto(tourBooking)).collect(Collectors.toList());
    }

    @PutMapping
    public BookingDto updateWithPut(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        LOGGER.info("PUT /tours/{}/bookings", tourId);
         return toDto(tourBookingService.update(tourId, bookingDto.getCustomerId(),
                 bookingDto.getDate(), bookingDto.getPickupLocation()));
    }

    @PatchMapping
    public BookingDto updateWithPatch(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated BookingDto bookingDto) {
        LOGGER.info("PATCH /tours/{}/bookings", tourId);
        return toDto(tourBookingService.updateSome(tourId, bookingDto.getCustomerId(),
                 bookingDto.getDate(), bookingDto.getPickupLocation()));
    }

    @DeleteMapping("/{customerId}")
    public void delete(@PathVariable(value = "tourId") int tourId, @PathVariable(value = "customerId") int customerId) {
        LOGGER.info("DELETE /tours/{}/bookings", tourId);
        tourBookingService.delete(tourId, customerId);
    }

    private BookingDto toDto(TourBooking tourBooking) {
        return new BookingDto(tourBooking.getDate(), tourBooking.getPickupLocation(), tourBooking.getCustomerId(),
                tourBooking.getPartisipants(), tourBooking.getTotalPriceString());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();

    }

}
