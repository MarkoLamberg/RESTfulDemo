package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.service.TourBookingService;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.web.ExpandedBookingDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(path = "/tours/bookings")
public class TourAllBookingsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TourAllBookingsController.class);
    @Autowired
    private TourBookingService tourBookingService;

    @GetMapping
    public List<ExpandedBookingDto> getAllBookings() {
        LOGGER.info("GET /tours/bookings/");
        List<TourBooking> tourBookings = tourBookingService.lookupBookingsAfter(0);
        return tourBookings.stream().map(tourBooking -> toExpandedDto(tourBooking)).collect(Collectors.toList());
    }

    @DeleteMapping("/{customerId}")
    public void delete(@PathVariable(value = "customerId") int customerId) {
        LOGGER.info("DELETE /tours/bookings/{}", customerId);
        tourBookingService.delete(customerId);
    }

    @DeleteMapping
    public void delete() {
        LOGGER.info("DELETE /tours/bookings/");
        tourBookingService.deleteAll();
    }

    private ExpandedBookingDto toExpandedDto(TourBooking tourBooking) {
        return new ExpandedBookingDto(tourBooking.getDate(), tourBooking.getPickupLocation(), tourBooking.getCustomerId(),
                tourBooking.getPartisipants(), tourBooking.getTotalPriceString(), tourBooking.getTour().getId());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();

    }

}
