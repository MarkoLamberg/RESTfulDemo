package com.bookinggo.RestfulDemo.controller;

import com.bookinggo.RestfulDemo.dto.*;
import com.bookinggo.RestfulDemo.entity.TourBooking;
import com.bookinggo.RestfulDemo.exception.TourBookingServiceException;
import com.bookinggo.RestfulDemo.exception.TourServiceException;
import com.bookinggo.RestfulDemo.service.TourBookingService;
import com.bookinggo.RestfulDemo.service.TourService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@RestController
@RequestMapping("/tours")
@Api(tags = "Tour Booking")
@Slf4j
@RequiredArgsConstructor
public class TourBookingController {

    private final TourBookingService tourBookingService;
    private final TourService tourService;
    private final ModelMapper modelMapper;

    @ApiOperation(value = "Create a new tour booking")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully creating a new booking", response = TourBooking.class),
            @ApiResponse(code = 400, message = "Failed creating a new booking")
    })
    @PostMapping("/{tourId}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookingDto> createTourBooking(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody BookingDto bookingDto) throws TourBookingServiceException {
        log.info("POST /tours/{}/bookings: {}", tourId, bookingDto.toString());
        try {
            tourService.getTourById(tourId);
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        final LocalDateTime pickupDateTime = LocalDateTime.parse(bookingDto.getPickupDateTime(), ISO_LOCAL_DATE_TIME);

        try {
            final TourBooking createdBooking = tourBookingService.createBooking(tourId, bookingDto.getCustomerId(), pickupDateTime,
                    bookingDto.getPickupLocation(), bookingDto.getParticipants());

            return ResponseEntity
                    .created(URI.create("/tours/" + tourId + "/bookings"))
                    .body(toDto(createdBooking));
        } catch (TourBookingServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all bookings for a tour by tour id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully getting all bookings for a tour", response = TourBooking.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Failed getting bookings for a tour")
    })
    @GetMapping("/{tourId}/bookings")
    public ResponseEntity<List<BookingDto>> getAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        log.info("GET /tours/{}/bookings", tourId);
        try {
            tourService.getTourById(tourId);
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        try {
            final List<TourBooking> tourBookings = tourBookingService.getBookingsByTourId(tourId);
            return ResponseEntity
                    .ok()
                    .body(tourBookings
                            .stream()
                            .map(this::toDto)
                            .collect(Collectors.toList()));
        } catch (TourBookingServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all bookings")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully getting all bookings", response = ExpandedBookingDto.class)
    })
    @GetMapping("/bookings")
    public List<ExpandedBookingDto> getAllBookings() {
        log.info("GET /tours/bookings");
        final List<TourBooking> tourBookings = tourBookingService.getAllBookings();

        return tourBookings.stream().map(this::toExpandedDto).collect(Collectors.toList());
    }

    @ApiOperation(value = "Update booking by tour id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updating existing booking", response = BookingDto.class),
            @ApiResponse(code = 400, message = "Failed updating existing booking")
    })
    @PutMapping("/{tourId}/bookings")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody BookingPatchDto bookingPatchDto) {
        log.info("PUT /tours/{}/bookings: {}", tourId, bookingPatchDto.toString());
        try {
            tourService.getTourById(tourId);
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        LocalDateTime pickupDateTime = null;

        if (bookingPatchDto.getPickupDateTime() != null) {
            pickupDateTime = LocalDateTime.parse(bookingPatchDto.getPickupDateTime(), ISO_LOCAL_DATE_TIME);
        }

        try {
            final TourBooking response = tourBookingService.updateBooking(tourId, bookingPatchDto.getCustomerId(),
                    pickupDateTime, bookingPatchDto.getPickupLocation(), bookingPatchDto.getParticipants());

            return ResponseEntity
                    .ok()
                    .body(toDto(response));

        } catch (TourBookingServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Delete all bookings for a tour by tour id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleting all bookings for a tour", response = BookingDto.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Failed deleting all bookings for a tour")
    })
    @DeleteMapping("/{tourId}/bookings")
    public ResponseEntity<List<BookingDto>> deleteAllBookingsForTour(@PathVariable(value = "tourId") int tourId) {
        log.info("DELETE /tours/{}/bookings", tourId);
        try {
            tourService.getTourById(tourId);
            final List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithTourId(tourId);

            return ResponseEntity
                    .ok()
                    .body(listOfDtos(bookings));
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Delete all bookings for a tour and a customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleting all bookings for a tour and a customer", response = ExpandedBookingDto.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Failed deleting bookings for a tour and a customer")
    })
    @DeleteMapping("/{tourId}/bookings/{customerId}")
    public ResponseEntity<List<ExpandedBookingDto>> deleteAllBookingsForTourAndCustomer(@PathVariable(value = "tourId") int tourId, @PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/{}/bookings/{}", tourId, customerId);
        try {
            tourService.getTourById(tourId);
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        try {
            final List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(tourId, customerId);
            return ResponseEntity
                    .ok()
                    .body(listOfExpandedDtos(bookings));
        } catch (TourBookingServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Delete all bookings for a customer by customer id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleting all bookings for a customer", response = ExpandedBookingDto.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Failed deleting all bookings for a customer")
    })
    @DeleteMapping("/bookings/{customerId}")
    public ResponseEntity<List<ExpandedBookingDto>> deleteAllBookingsForCustomer(@PathVariable(value = "customerId") int customerId) {
        log.info("DELETE /tours/bookings/{}", customerId);
        try {
            final List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithCustomerId(customerId);
            return ResponseEntity
                    .ok()
                    .body(listOfExpandedDtos(bookings));
        } catch (TourBookingServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Delete all bookings")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleting all bookings", response = ExpandedBookingDto.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Failed deleting all bookings")
    })
    @DeleteMapping("/bookings")
    public List<ExpandedBookingDto> deleteAllBookings() {
        log.info("DELETE /tours/bookings");
        try {
            final List<TourBooking> bookings = tourBookingService.deleteAllBookings();
            return listOfExpandedDtos(bookings);
        } catch (TourBookingServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private BookingDto toDto(TourBooking tourBooking) {
        return modelMapper.map(tourBooking, BookingDto.class);
    }

    private List<BookingDto> listOfDtos(List<TourBooking> bookings) {
        final List<BookingDto> bookingDtos = new LinkedList<>();
        bookings.forEach(b -> bookingDtos.add(toDto(b)));

        return bookingDtos;
    }

    private ExpandedBookingDto toExpandedDto(TourBooking tourBooking) {
        return modelMapper.map(tourBooking, ExpandedBookingDto.class);
    }

    private List<ExpandedBookingDto> listOfExpandedDtos(List<TourBooking> bookings) {
        final List<ExpandedBookingDto> bookingDtos = new LinkedList<>();
        bookings.forEach(b -> bookingDtos.add(toExpandedDto(b)));

        return bookingDtos;
    }
}
