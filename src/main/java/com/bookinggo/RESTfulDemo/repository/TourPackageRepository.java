package com.bookinggo.RestfulDemo.repository;

import com.bookinggo.RestfulDemo.entity.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourPackageRepository extends JpaRepository<TourPackage, Integer> {

    Optional<TourPackage> getTourPackageByCode(String code);
}
