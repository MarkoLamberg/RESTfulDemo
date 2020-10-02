package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.dto.TourDto;
import com.bookinggo.RESTfulDemo.dto.TourPatchDto;
import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.exception.TourBookingServiceException;
import com.bookinggo.RESTfulDemo.exception.TourServiceException;
import com.bookinggo.RESTfulDemo.service.TourBookingService;
import com.bookinggo.RESTfulDemo.service.TourService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tours")
@Slf4j
@AllArgsConstructor
public class TourController {

    private final TourService tourService;

    private final TourBookingService tourBookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createTour(@Valid @RequestBody TourDto tourDto) {
        log.info("POST /tours: {}", tourDto.toString());
        final Optional<Tour> existingTour = tourService.getTourByTourPackageCodeAndTitle(tourDto.getTourPackageCode(), tourDto.getTitle());

        if (existingTour.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't create tour. Tour with that Tour Package Code and Tour Title already exists.");
        }

        final Optional<Tour> tour = tourService.createTour(tourDto.getTourPackageCode(), tourDto.getTitle(), tourDto.getDuration(), tourDto.getPrice());
        return ResponseEntity
                .created(URI.create("/tours"))
                .body(tour.get());
    }

    @PutMapping("/{tourId}")
    public ResponseEntity<?> updateTour(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody TourPatchDto tourPatchDto) {
        log.info("PUT /tours/{}: {}", tourId, tourPatchDto.toString());
        try {
            final Optional<Tour> tour = tourService.getTourById(tourId);
            final Optional<Tour> tourWithNewTitleOrTourPackage = getTourWithNewTitleOrTourPackage(tourPatchDto, tour);

            if (tourWithNewTitleOrTourPackage.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't update tour. Can't change the tour name to match with other existing tour.");
            } else {
                final Optional<Tour> response = tourService.updateTour(tourId, tourPatchDto.getTourPackageCode(), tourPatchDto.getTitle(), tourPatchDto.getDuration(), tourPatchDto.getPrice());
                return ResponseEntity
                        .ok()
                        .body(response.get());
            }
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public List<Tour> getAllTours() {
        log.info("GET /tours");
        return tourService.getAllTours();
    }

    @GetMapping(path = "/{tourId}")
    public ResponseEntity<?> getTourById(@PathVariable(value = "tourId") int tourId) {
        log.info("GET /tours/{}", tourId);
        try {
            final Optional<Tour> tour = tourService.getTourById(tourId);
            return ResponseEntity
                    .ok()
                    .body(tour.get());
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(path = "/byLocation/{tourLocation}")
    public ResponseEntity<?> getToursByLocation(@PathVariable(value = "tourLocation") String location) {
        log.info("GET /tours/byLocation/{}", location);
        try {
            final List<Tour> tours = tourService.getToursByLocation(location);
            return ResponseEntity
                    .ok()
                    .body(tours);
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{tourId}")
    public ResponseEntity<?> deleteTour(@PathVariable(value = "tourId") int tourId) {
        log.info("DELETE /tours/{}", tourId);

        try {
            if (tourBookingService.getBookingsByTourId(tourId).size() > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't delete tour that has bookings.");
            }
        } catch (TourBookingServiceException e) {
        }

        try {
            final Optional<Tour> deletedTour = tourService.deleteTourById(tourId);
            return ResponseEntity
                    .ok()
                    .body(deletedTour.get());
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private Optional<Tour> getTourWithNewTitleOrTourPackage(@RequestBody @Valid TourPatchDto tourPatchDto, Optional<Tour> tour) {
        if (((tourPatchDto.getTitle() == null) || tour.get().getTitle().equals(tourPatchDto.getTitle())) &&
                ((tourPatchDto.getTourPackageCode() == null) || tour.get().getTourPackage().getCode().equals(tourPatchDto.getTourPackageCode()))) {
            return Optional.empty();
        }

        return tourService.getTourByTourPackageCodeAndTitle(
                (tourPatchDto.getTourPackageCode() == null) ? tour.get().getTourPackage().getCode() : tourPatchDto.getTourPackageCode(),
                (tourPatchDto.getTitle() == null) ? tour.get().getTitle() : tourPatchDto.getTitle());
    }
}
