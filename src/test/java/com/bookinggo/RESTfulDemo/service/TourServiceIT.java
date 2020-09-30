package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TourServiceIT extends AbstractRESTfulDemoIT {

    private static final int TOUR_ID = 1;

    private static final String TOUR_PACKAGE_CODE = "LS";

    private static final String TOUR_TITLE = "London Tower Bridge";

    private static final String ORIGINAL_TOUR_TITLE = "Arsenal Football Tour";

    private static final String TOUR_DURATION = "2.5 hours";

    private static final String ORIGINAL_TOUR_DURATION = "3 hours";

    private static final int TOUR_PRICE = 150;

    private static final int ORIGINAL_TOUR_PRICE = 50;

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
    public void shouldUpdateTour_whenUpdateTour_givenValidTour() {
        Optional<Tour> tourBefore = tourService.getTourById(TOUR_ID);
        assertThat(tourBefore.get().getTitle()).isNotEqualTo(TOUR_TITLE);
        assertThat(tourBefore.get().getDuration()).isNotEqualTo(TOUR_DURATION);
        assertThat(tourBefore.get().getPrice()).isNotEqualTo(TOUR_PRICE);

        tourService.updateTour(TOUR_ID, null, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE);

        Optional<Tour> tourAfter = tourService.getTourById(TOUR_ID);
        assertThat(tourAfter.get().getTitle()).isEqualTo(TOUR_TITLE);
        assertThat(tourAfter.get().getDuration()).isEqualTo(TOUR_DURATION);
        assertThat(tourAfter.get().getPrice()).isEqualTo(TOUR_PRICE);
    }

    @Sql
    @ParameterizedTest
    @MethodSource("titleAndDurationAndPriceAndTourProvider")
    public void parameterized_shouldUpdateTour_whenUpdateTour_givenValidTour(String title, String duration, Integer price, Tour updatedTour) {
        tourService.updateTour(TOUR_ID, null, title, duration, price);

        Optional<Tour> tourAfter = tourService.getTourById(TOUR_ID);
        assertThat(tourAfter.get().getTitle()).isEqualTo(updatedTour.getTitle());
        assertThat(tourAfter.get().getDuration()).isEqualTo(updatedTour.getDuration());
        assertThat(tourAfter.get().getPrice()).isEqualTo(updatedTour.getPrice());
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

    private static Stream<Arguments> titleAndDurationAndPriceAndTourProvider() {
        return Stream.of(
                Arguments.of(null, null, null,
                        buildTour(ORIGINAL_TOUR_TITLE, ORIGINAL_TOUR_DURATION, ORIGINAL_TOUR_PRICE)),
                Arguments.of(TOUR_TITLE, null, null,
                        buildTour(TOUR_TITLE, ORIGINAL_TOUR_DURATION, ORIGINAL_TOUR_PRICE)),
                Arguments.of(null, TOUR_DURATION, null,
                        buildTour(ORIGINAL_TOUR_TITLE, TOUR_DURATION, ORIGINAL_TOUR_PRICE)),
                Arguments.of(null, null, TOUR_PRICE,
                        buildTour(ORIGINAL_TOUR_TITLE, ORIGINAL_TOUR_DURATION, TOUR_PRICE)),
                Arguments.of(TOUR_TITLE, TOUR_DURATION, null,
                        buildTour(TOUR_TITLE, TOUR_DURATION, ORIGINAL_TOUR_PRICE)),
                Arguments.of(TOUR_TITLE, null, TOUR_PRICE,
                        buildTour(TOUR_TITLE, ORIGINAL_TOUR_DURATION, TOUR_PRICE)),
                Arguments.of(null, TOUR_DURATION, TOUR_PRICE,
                        buildTour(ORIGINAL_TOUR_TITLE, TOUR_DURATION, TOUR_PRICE)),
                Arguments.of(TOUR_TITLE, TOUR_DURATION, TOUR_PRICE,
                        buildTour(TOUR_TITLE, TOUR_DURATION, TOUR_PRICE)));
    }

    static private Tour buildTour(String title, String duration, Integer price) {
        return Tour.builder()
                .title(title)
                .duration(duration)
                .price(price)
                .build();
    }
}
