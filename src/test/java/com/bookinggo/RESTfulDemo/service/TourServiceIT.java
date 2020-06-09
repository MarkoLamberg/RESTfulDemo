package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class TourServiceIT extends AbstractRESTfulDemoIT {

    private static final int TOUR_ID = 1;

    @Autowired
    private TourService tourService;

    @Test
    public void shouldReturnEightTours_whenLookupAllTours_givenToursExist() {
        List<Tour> tours = tourService.lookupAllTours();

        assertEquals(8, tours.size());
    }

    @Test
    public void shouldReturnATour_whenLookupTourById_givenTourWithIdExists() {
        Optional<Tour> tour = tourService.lookupTourById(TOUR_ID);

        assertEquals(true, tour.isPresent());
    }
}
