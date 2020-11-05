package com.bookinggo.RestfulDemo.repository;

import com.bookinggo.RestfulDemo.RepositoryTests;
import com.bookinggo.RestfulDemo.entity.Tour;
import com.bookinggo.RestfulDemo.entity.TourPackage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TourRepositoryIT extends AbstractRepositoryIT implements RepositoryTests {

    private static final String TOUR_PACKAGE_CODE = "LS";

    private static final String TOUR_TITLE = "London City Sightseeing Tour";

    @Autowired
    TourRepository tourRepository;

    @Autowired
    TourPackageRepository tourPackageRepository;

    @Test
    public void shouldReturnTour_whenFindTourByTourPackageAndTitle_givenTourExists() {
        Optional<TourPackage> tourPackage = tourPackageRepository.getTourPackageByCode(TOUR_PACKAGE_CODE);
        assertThat(tourPackage).isPresent();

        Optional<Tour> tour = tourRepository.findTourByTourPackageAndTitle(tourPackage.get(), TOUR_TITLE);
        assertThat(tour).isPresent();
    }
}
