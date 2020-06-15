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

    private static final String TOUR_TITLE = "London City Sightseeing Tour";

    @Autowired
    private TourService tourService;

    @MockBean
    private TourRepository tourRepositoryMock;

    @MockBean
    private TourPackageRepository tourPackageRepositoryMock;

    @Test
    public void shouldCallFindAll_whenLookupAllTours_givenNoToursExist() {
        List<Tour> tours = tourService.lookupAllTours();
        verify(tourRepositoryMock, times(1)).findAll();
        assertThat(tours.size()).isEqualTo(0);
    }

    @Test
    public void shouldCallFindById_whenLookupTourById_givenNoTourWithIdExists() {
        Optional<Tour> tour = tourService.lookupTourById(TOUR_ID);
        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        assertThat(tour).isEmpty();
    }

    @Test
    public void shouldCallFindById_whenLookupTourPackageCodeAndTitle_givenNoTourWithThatPackageCodeAndTitleExists() {
        Optional<Tour> tour = tourService.lookupTourByTourPackageCodeAndTitle(TOUR_PACKAGE_CODE, TOUR_TITLE);

        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(TOUR_PACKAGE_CODE);
        assertThat(tour).isEmpty();
    }

}
