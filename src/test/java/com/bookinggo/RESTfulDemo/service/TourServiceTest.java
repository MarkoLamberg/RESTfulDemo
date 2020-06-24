package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.repository.TourPackageRepository;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TourServiceTest {

    private static final int TOUR_ID = 234;

    private static final String TOUR_PACKAGE_CODE = "LS";

    private static final String NON_EXISTING_TOUR_PACKAGE_CODE = "LS";

    private static final String TOUR_TITLE = "London City Sightseeing Tour";

    private static final String TOUR_DURATION = "2 hours";

    private static final int TOUR_PRICE = 150;

    private static final String TOUR_LOCATION = "liverpool";

    @Autowired
    private TourService tourService;

    @MockBean
    private TourRepository tourRepositoryMock;

    @MockBean
    private TourPackageRepository tourPackageRepositoryMock;

    @Test
    public void shouldCallGetTourPackageByCode_whenCreateTour_givenTourPackageDoesntExist() {
        Optional<Tour> tour = tourService.createTour(NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE);

        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(TOUR_PACKAGE_CODE);
        assertThat(tour).isEmpty();
    }

    @Test
    public void shouldCallFindAll_whenGetAllTours_givenNoToursExist() {
        List<Tour> tours = tourService.getAllTours();

        verify(tourRepositoryMock, times(1)).findAll();
        assertThat(tours.size()).isEqualTo(0);
    }

    @Test
    public void shouldCallFindById_whenGetTourById_givenNoTourWithIdExists() {
        Optional<Tour> tour = tourService.getTourById(TOUR_ID);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        assertThat(tour).isEmpty();
    }

    @Test
    public void shouldCallFindAll_whenGetToursByLocation_givenNoTourWithLocationExists() {
        List<Tour> tours = tourService.getToursByLocation(TOUR_LOCATION);

        verify(tourRepositoryMock, times(1)).findAll();
        assertThat(tours.size()).isEqualTo(0);
    }

    @Test
    public void shouldCallGetTourPackageByCode_whenGetTourPackageCodeAndTitle_givenNoTourWithThatPackageCodeAndTitleExists() {
        Optional<Tour> tour = tourService.getTourByTourPackageCodeAndTitle(TOUR_PACKAGE_CODE, TOUR_TITLE);

        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(TOUR_PACKAGE_CODE);
        assertThat(tour).isEmpty();
    }

    @Test
    public void shouldCallDeleteById_whenDeleteTourById() {
        tourService.deleteTourById(TOUR_ID);

        verify(tourRepositoryMock, times(1)).deleteById(TOUR_ID);
    }
}
