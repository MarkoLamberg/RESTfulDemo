package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface TourBookingRepository extends CrudRepository<TourBooking, Integer> {

    List<TourBooking> findByTourId(Integer tourId);

    Page<TourBooking> findByTourId(Integer tourId, Pageable pageable);

    Optional<TourBooking> findByTourIdAndCustomerId(Integer tourId, Integer customerId);

    Page<TourBooking> findAllByCustomerIdAfter(Integer tourId, Pageable pageable);
}