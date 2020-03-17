package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface TourBookingRepository extends JpaRepository<TourBooking, Integer> {

    List<TourBooking> findByTourId(Integer tourId);

    TourBooking findByTourIdAndCustomerId(Integer tourId, Integer customerId);

    List<TourBooking> findByCustomerId(Integer customerId);
}