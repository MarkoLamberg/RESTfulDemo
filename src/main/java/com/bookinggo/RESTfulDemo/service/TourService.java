package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;

import java.util.List;
import java.util.Optional;

public interface TourService {

    Optional<Tour> createTour(String tourPackageCode, String title, String duration, int price);

    Optional<Tour> updateTour(int tourId, String tourPackageCode, String title, String duration, Integer price);

    List<Tour> getAllTours();

    Optional<Tour> getTourById(int tourId);

    List<Tour> getToursByLocation(String location);

    Optional<Tour> getTourByTourPackageCodeAndTitle(String tourPackageCode, String title);

    Optional<Tour> deleteTourById(int tourId);
}
