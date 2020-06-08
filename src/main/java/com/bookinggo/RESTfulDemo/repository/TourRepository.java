package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour,Integer> {

}
