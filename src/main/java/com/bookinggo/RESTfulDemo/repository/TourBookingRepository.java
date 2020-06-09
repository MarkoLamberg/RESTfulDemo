package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourBookingRepository extends JpaRepository<TourBooking, Integer> {

    List<TourBooking> findByTourId(Integer tourId);

    List<TourBooking> findByTourIdAndCustomerId(Integer tourId, Integer customerId);

    List<TourBooking> findByCustomerId(Integer customerId);
}