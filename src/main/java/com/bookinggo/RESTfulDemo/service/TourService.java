package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;

import java.util.List;
import java.util.Optional;

public interface TourService {

    public List<Tour> lookupAllTours();

    public Optional<Tour> lookupTourById(int id);

    public List<Tour> lookupToursByLocation(String location);
}
