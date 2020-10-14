package com.bookinggo.RestfulDemo.repository;

import com.bookinggo.RestfulDemo.entity.Tour;
import com.bookinggo.RestfulDemo.entity.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour,Integer> {

    Optional<Tour> findTourByTourPackageAndTitle(TourPackage tourPackage, String title);
}
