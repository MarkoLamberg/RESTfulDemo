package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface TourBookingRepository extends CrudRepository<TourBooking, Integer> {

    List<TourBooking> findByTourId(Integer tourId);

    Optional<TourBooking> findByTourIdAndCustomerId(Integer tourId, Integer customerId);

    Optional<List<TourBooking>> findByCustomerId(Integer customerId);

    List<TourBooking> findAllByCustomerIdAfter(Integer tourId);
}