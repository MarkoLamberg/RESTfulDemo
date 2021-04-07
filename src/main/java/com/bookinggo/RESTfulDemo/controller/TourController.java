package com.bookinggo.RestfulDemo.controller;

import com.bookinggo.RestfulDemo.dto.TourDto;
import com.bookinggo.RestfulDemo.dto.TourPatchDto;
import com.bookinggo.RestfulDemo.entity.Tour;
import com.bookinggo.RestfulDemo.exception.TourBookingServiceException;
import com.bookinggo.RestfulDemo.exception.TourServiceException;
import com.bookinggo.RestfulDemo.service.TourBookingService;
import com.bookinggo.RestfulDemo.service.TourService;
import com.bookinggo.RestfulDemo.service.amazon.DynamoDBService;
import io.swagger.annotations.*;
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

import static com.bookinggo.RestfulDemo.service.TourServiceImpl.tourDynamoDBTableName;

@RestController
@RequestMapping("/tours")
@Api(tags = "Tour")
@Slf4j
@AllArgsConstructor
public class TourController {

    private final TourService tourService;
    private final TourBookingService tourBookingService;
    private final DynamoDBService dynamoDBService;

    @ApiOperation(value = "Create a new tour")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully creating a new tour", response = Tour.class),
            @ApiResponse(code = 400, message = "Failed creating a new tour")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Tour> createTour(@Valid @RequestBody TourDto tourDto) {
        log.info("POST /tours: {}", tourDto.toString());
        try {
            tourService.getTourByTourPackageCodeAndTitle(tourDto.getTourPackageCode(), tourDto.getTitle());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't create tour. Tour with that Tour Package Code and Tour Title already exists.");
        } catch (TourServiceException e) {
            log.info("createTour - caught TourBookingServiceException - Not yet Tour with given title and Tour Package.");
        }

        try {
            final Tour tour = tourService.createTour(tourDto.getTourPackageCode(), tourDto.getTitle(), tourDto.getDuration(), tourDto.getPrice());
            return ResponseEntity
                    .created(URI.create("/tours"))
                    .body(tour);
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Update tour by tour id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updating existing tour", response = Tour.class),
            @ApiResponse(code = 400, message = "Failed updating existing tour")
    })
    @PutMapping("/{tourId}")
    public ResponseEntity<Tour> updateTour(@PathVariable(value = "tourId") int tourId, @Valid @RequestBody TourPatchDto tourPatchDto) {
        log.info("PUT /tours/{}: {}", tourId, tourPatchDto.toString());
        try {
            final Tour tour = tourService.getTourById(tourId);
            final Optional<Tour> tourWithNewTitleOrTourPackage = getTourWithNewTitleOrTourPackage(tourPatchDto, tour);

            if (tourWithNewTitleOrTourPackage.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't update tour. Can't change the tour name to match with other existing tour.");
            } else {
                final Tour response = tourService.updateTour(tourId, tourPatchDto.getTourPackageCode(), tourPatchDto.getTitle(), tourPatchDto.getDuration(), tourPatchDto.getPrice());
                return ResponseEntity
                        .ok()
                        .body(response);
            }
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all tours", response = Tour.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully getting all tours", response = Tour.class, responseContainer = "List"),
    })
    @GetMapping
    public List<Tour> getAllTours() {
        log.info("GET /tours");
        return tourService.getAllTours();
    }

    @ApiOperation(value = "Get tour by tour id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully getting tour", response = Tour.class),
            @ApiResponse(code = 400, message = "Failed getting tour")
    })
    @GetMapping(path = "/{tourId}")
    public ResponseEntity<Tour> getTourById(@PathVariable(value = "tourId") int tourId) {
        log.info("GET /tours/{}", tourId);
        try {
            final Tour tour = tourService.getTourById(tourId);
            return ResponseEntity
                    .ok()
                    .body(tour);
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all tours by tour location")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully getting tours by location", response = Tour.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Failed getting tours by location")
    })
    @GetMapping(path = "/byLocation/{tourLocation}")
    public ResponseEntity<List<Tour>> getToursByLocation(@PathVariable(value = "tourLocation") String location) {
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

    @ApiOperation(value = "Delete tour by tour id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleting tour", response = Tour.class),
            @ApiResponse(code = 400, message = "Failed deleting tour")
    })
    @DeleteMapping("/{tourId}")
    public ResponseEntity<Tour> deleteTour(@PathVariable(value = "tourId") int tourId) {
        log.info("DELETE /tours/{}", tourId);

        try {
            if (tourBookingService.getBookingsByTourId(tourId).size() > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't delete tour that has bookings.");
            }
        } catch (TourBookingServiceException e) {
            log.info("deleteTour - caught TourBookingServiceException - No booking for this tour.");
        }

        try {
            final Tour deletedTour = tourService.deleteTourById(tourId);
            return ResponseEntity
                    .ok()
                    .body(deletedTour);
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get DynamoDB dump for all tours created")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully getting dump for created tours", response = String.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Failed getting dump for created tours")
    })
    @GetMapping(path = "/dump")
    public ResponseEntity<List<String>> getToursDynamoDBTable() {
        log.info("GET /dump");
        try {
            final List<String> list = dynamoDBService.dumpTable(tourDynamoDBTableName);
            return ResponseEntity
                    .ok()
                    .body(list);
        } catch (TourServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private Optional<Tour> getTourWithNewTitleOrTourPackage(@RequestBody @Valid TourPatchDto tourPatchDto, Tour tour) {
        if (((tourPatchDto.getTitle() == null) || tour.getTitle().equals(tourPatchDto.getTitle())) &&
                ((tourPatchDto.getTourPackageCode() == null) || tour.getTourPackage().getCode().equals(tourPatchDto.getTourPackageCode()))) {
            return Optional.empty();
        }

        try {
            Tour existingTour = tourService.getTourByTourPackageCodeAndTitle(
                    (tourPatchDto.getTourPackageCode() == null) ? tour.getTourPackage().getCode() : tourPatchDto.getTourPackageCode(),
                    (tourPatchDto.getTitle() == null) ? tour.getTitle() : tourPatchDto.getTitle());
            return Optional.of(existingTour);
        } catch (TourServiceException e) {
            return Optional.empty();
        }
    }
}
