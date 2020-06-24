package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;

import java.util.List;
import java.util.Optional;

public interface TourService {

    public Optional<Tour> createTour(String tourPackageCode, String title, String duration, int price);

    public List<Tour> getAllTours();

    public Optional<Tour> getTourById(int tourId);

    public List<Tour> getToursByLocation(String location);

    public Optional<Tour> getTourByTourPackageCodeAndTitle(String tourPackageCode, String title);

    public void deleteTourById(int tourId);
}
