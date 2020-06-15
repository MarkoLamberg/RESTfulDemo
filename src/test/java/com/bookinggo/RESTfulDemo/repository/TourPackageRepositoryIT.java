package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.TourPackage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TourPackageRepositoryIT extends AbstractRepositoryIT {

    private static final String TOUR_PACKAGE_CODE = "LS";

    @Autowired
    TourPackageRepository tourPackageRepository;

    @Test
    public void shouldReturnTourPackage_whenGetTourPackageByCode_givenTourPackageExists() {
        Optional<TourPackage> tourPackage = tourPackageRepository.getTourPackageByCode(TOUR_PACKAGE_CODE);
        assertThat(tourPackage).isPresent();
    }
}
