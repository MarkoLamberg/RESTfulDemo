package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.TourRating;
import com.bookinggo.RESTfulDemo.entity.TourRatingPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface TourRatingRepository extends CrudRepository<TourRating, TourRatingPk> {

    List<TourRating> findByPkTourId(Integer tourId);

    Page<TourRating> findByPkTourId(Integer tourId, Pageable pageable);

    TourRating findByPkTourIdAndPkCustomerId(Integer tourId, Integer customerId);
}