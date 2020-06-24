package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.dto.ErrorDto;
import com.bookinggo.RESTfulDemo.dto.TourDto;
import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.service.TourBookingService;
import com.bookinggo.RESTfulDemo.service.TourService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/tours")
@Slf4j
@AllArgsConstructor
public class TourController {

    private final TourService tourService;

    private final TourBookingService tourBookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tour createTour(@Valid @RequestBody TourDto tourDto) {
        log.info("POST /tours");
        Optional<Tour> existingTour = tourService.getTourByTourPackageCodeAndTitle(tourDto.getTourPackageCode(), tourDto.getTitle());

        if (existingTour.isPresent()) {
            throw new ResponseStatusException(
                    BAD_REQUEST, "Tour with that Tour Package Code and Tour Title already exists.");
        }

        Optional<Tour> tour = tourService.createTour(tourDto.getTourPackageCode(), tourDto.getTitle(), tourDto.getDuration(), tourDto.getPrice());

        if (tour.isPresent()) {
            return tour.get();
        }

        throw new ResponseStatusException(
                BAD_REQUEST, "Tour doesn't exist. Provide correct Tour Package Id.");
    }

    @GetMapping
    public List<Tour> getAllTours() {
        log.info("GET /tours");
        return tourService.getAllTours();
    }

    @GetMapping(path = "/{tourId}")
    public Tour getToursById(@PathVariable(value = "tourId") int tourId) {
        log.info("GET /tours/{}", tourId);
        Optional<Tour> tour = tourService.getTourById(tourId);

        if (tour.isPresent()) {
            return tour.get();
        }

        throw new ResponseStatusException(
                BAD_REQUEST, "Tour doesn't exist. Provide correct Tour Id.");
    }

    @GetMapping(path = "/byLocation/{tourLocation}")
    public ResponseEntity<?> getToursByLocation(@PathVariable(value = "tourLocation") String location) {
        log.info("GET /tours/byLocation/{}", location);
        List<Tour> tours = tourService.getToursByLocation(location);

        if (tours.size() > 0) {
            return ResponseEntity
                    .ok()
                    .body(tours);
        }

        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.name().toLowerCase().replace('_', ' '))
                        .message("Tour with that location doesn't exist. Provide correct Tour Location.")
                        .path("/tours/byLocation/" + location)
                        .build());
    }

    @DeleteMapping("/{tourId}")
    public Tour deleteTour(@PathVariable(value = "tourId") int tourId) {
        log.info("DELETE /tours/{}", tourId);
        Optional<Tour> tour = tourService.getTourById(tourId);

        if (tour.isPresent()) {

            if (tourBookingService.getBookingsByTourId(tourId).size() > 0) {
                throw new ResponseStatusException(
                        BAD_REQUEST, "Can't delete tour that has bookings.");
            }

            tourService.deleteTourById(tourId);

            return tour.get();
        }

        throw new ResponseStatusException(
                BAD_REQUEST, "Tour doesn't exist. Provide correct Tour Id.");
    }
}
