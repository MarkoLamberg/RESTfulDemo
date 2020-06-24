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

    private static final String TOUR_DURATION = "2 hours";

    private static final int TOUR_PRICE = 150;

    private static final String TOUR_LOCATION = "paris";

    @Autowired
    private TourService tourService;

    @Sql
    @Test
    public void shouldCreateTour_whenCreateTour_givenValidTour() {
        List<Tour> toursBefore = tourService.getAllTours();
        assertThat(toursBefore.size()).isEqualTo(4);

        tourService.createTour(TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE);

        List<Tour> toursAfter = tourService.getAllTours();
        Tour tour = toursAfter.get(4);

        assertThat(toursAfter.size()).isEqualTo(5);
        assertThat(tour.getTourPackage().getCode()).isEqualTo(TOUR_PACKAGE_CODE);
        assertThat(tour.getTitle()).isEqualTo(TOUR_TITLE);
        assertThat(tour.getDuration()).isEqualTo(TOUR_DURATION);
        assertThat(tour.getPrice()).isEqualTo(TOUR_PRICE);
    }

    @Sql
    @Test
    public void shouldReturnFourTours_whenGetAllTours_givenToursExist() {
        List<Tour> tours = tourService.getAllTours();

        assertThat(tours.size()).isEqualTo(4);
    }

    @Sql
    @Test
    public void shouldReturnATour_whenGetTourById_givenTourWithIdExists() {
        Optional<Tour> tour = tourService.getTourById(TOUR_ID);

        assertThat(tour).isPresent();
    }

    @Sql
    @Test
    public void shouldReturnTwoTours_whenGetToursByLocation_givenToursWithLocationExist() {
        List<Tour> tours = tourService.getToursByLocation(TOUR_LOCATION);

        assertThat(tours.size()).isEqualTo(2);
    }

    @Sql
    @Test
    public void shouldReturnTour_whenGetTourByPackageCodeAndTitle_givenTourWithThatPackageCodeAndTitleExists() {
        Optional<Tour> tour = tourService.getTourByTourPackageCodeAndTitle(TOUR_PACKAGE_CODE, TOUR_TITLE);

        assertThat(tour).isPresent();
    }

    @Sql
    @Test
    public void shouldDeleteTour_whenDeleteTourById_givenTourWithTourIdExists() {
        Optional<Tour> tourBefore = tourService.getTourById(TOUR_ID);

        assertThat(tourBefore).isPresent();

        tourService.deleteTourById(TOUR_ID);

        Optional<Tour> tourAfter = tourService.getTourById(TOUR_ID);

        assertThat(tourAfter).isEmpty();
    }
}
