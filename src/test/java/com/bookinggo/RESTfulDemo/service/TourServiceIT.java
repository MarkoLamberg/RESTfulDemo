package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TourServiceIT extends AbstractRESTfulDemoIT {

    private static final int TOUR_ID = 1;
    private static final String TOUR_PACKAGE_CODE = "LS";

    private static final String TOUR_TITLE = "London Tower Bridge";

    private static final String DURATION = "2 hours";
    private static final int PRICE = 150;

    @Autowired
    private TourService tourService;

    @Sql
    @Test
    public void shouldCreateTour_whenCreateTour_givenValidTour() {
        List<Tour> toursBefore = tourService.lookupAllTours();
        assertThat(toursBefore.size()).isEqualTo(4);

        tourService.createTour(TOUR_PACKAGE_CODE, TOUR_TITLE, DURATION, PRICE);

        List<Tour> toursAfter = tourService.lookupAllTours();
        Tour tour = toursAfter.get(4);

        assertThat(toursAfter.size()).isEqualTo(5);
        assertThat(tour.getTourPackage().getCode()).isEqualTo(TOUR_PACKAGE_CODE);
        assertThat(tour.getTitle()).isEqualTo(TOUR_TITLE);
        assertThat(tour.getDuration()).isEqualTo(DURATION);
        assertThat(tour.getPrice()).isEqualTo(PRICE);
    }

    @Sql
    @Test
    public void shouldReturnEightTours_whenLookupAllTours_givenToursExist() {
        List<Tour> tours = tourService.lookupAllTours();

        assertThat(tours.size()).isEqualTo(4);
    }

    @Test
    public void shouldReturnATour_whenLookupTourById_givenTourWithIdExists() {
        Optional<Tour> tour = tourService.lookupTourById(TOUR_ID);

        assertThat(tour).isPresent();
    }

    @Sql
    @Test
    public void shouldReturnTour_whenLookupTourPackageCodeAndTitle_givenTourWithThatPackageCodeAndTitleExists() {
        Optional<Tour> tour = tourService.lookupTourByTourPackageCodeAndTitle(TOUR_PACKAGE_CODE, TOUR_TITLE);

        assertThat(tour).isPresent();
    }
}
