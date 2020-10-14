package com.bookinggo.RestfulDemo.service;

import com.bookinggo.RestfulDemo.entity.Tour;

import java.util.List;

public interface TourService {

    Tour createTour(String tourPackageCode, String title, String duration, int price);

    Tour updateTour(int tourId, String tourPackageCode, String title, String duration, Integer price);

    List<Tour> getAllTours();

    Tour getTourById(int tourId);

    List<Tour> getToursByLocation(String location);

    Tour getTourByTourPackageCodeAndTitle(String tourPackageCode, String title);

    Tour deleteTourById(int tourId);
}
