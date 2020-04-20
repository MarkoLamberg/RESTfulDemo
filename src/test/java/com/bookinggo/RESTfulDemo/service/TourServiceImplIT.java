package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class TourServiceImplIT extends AbstractRESTfulDemoIT {

    private static final int TOUR_ID = 1;

    @Autowired
    private TourServiceImpl service;

    @Test
    public void shouldReturnAllTours_whenLookupTours_givenToursExist() {
        List<Tour> tours = service.lookupAllTours();

        assertEquals(8, tours.size());
    }

    @Test
    public void shouldReturnATour_whenLookupTourById_givenTourWithIdExists() {
        Optional<Tour> tour = service.lookupTourById(TOUR_ID);

        assertEquals(true, tour.isPresent());
    }
}
