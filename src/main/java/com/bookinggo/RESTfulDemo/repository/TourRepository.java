package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "tours", path = "tours")
public interface TourRepository extends JpaRepository<Tour,Integer> {

}
