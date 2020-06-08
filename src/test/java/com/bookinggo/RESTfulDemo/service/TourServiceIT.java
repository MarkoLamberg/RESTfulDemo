package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("integTest")
public class TourServiceIT extends AbstractRESTfulDemoIT {

    private static final int TOUR_ID = 1;

    @Autowired
    private TourService service;

    @Test
    public void shouldReturnAllTours_whenLookupAllTours_givenToursExist() {
        List<Tour> tours = service.lookupAllTours();

        assertEquals(8, tours.size());
    }

    @Test
    public void shouldReturnATour_whenLookupTourById_givenTourWithIdExists() {
        Optional<Tour> tour = service.lookupTourById(TOUR_ID);

        assertEquals(true, tour.isPresent());
    }
}
