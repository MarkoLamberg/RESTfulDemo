package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourPackage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TourRepositoryIT extends AbstractRepositoryIT {

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
