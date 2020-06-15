package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.dto.TourDto;
import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.service.TourService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/tours")
@Slf4j
@AllArgsConstructor
public class TourController {

    private final TourService tourService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tour createTour(@Valid @RequestBody TourDto tourDto) {
        log.info("POST /tours");

        Optional<Tour> tour = tourService.createTour(tourDto.getTourPackageCode(), tourDto.getTitle(), tourDto.getDuration(), tourDto.getPrice());

        if (tour.isPresent()) {
            return tour.get();
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Tour Package Id");
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

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Tour Id");
    }

    @GetMapping(path = "/byLocation/{tourLocation}")
    public List<Tour> getToursByLocation(@PathVariable(value = "tourLocation") String location) {
        log.info("GET /tours/{}", location);
        List<Tour> tours = tourService.lookupToursByLocation(location);

        if (tours.size() > 0) {
            return tours;
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Provide correct Tour Location");
    }
}
