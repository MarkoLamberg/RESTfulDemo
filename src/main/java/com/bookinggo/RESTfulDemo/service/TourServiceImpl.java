package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;

    @Override
    public List<Tour> lookupAllTours() {
        return tourRepository.findAll();
    }

    @Override
    public Optional<Tour> lookupTourById(int id) {
        log.info("lookupTours - packageId: {}", id);
        Optional<Tour> tour = tourRepository.findById(id);

        return tour;
    }

    @Override
    public List<Tour> lookupToursByLocation(String location) {
        log.info("lookupTours - packageLocation: {}", location);
        List<Tour> tours = tourRepository.findAll();
        List<Tour> toursByLocation = tours.stream()
                .filter(tour -> tour.getTourPackage().getLocation().equalsIgnoreCase(location))
                .collect(Collectors.toList());

        return toursByLocation;
    }
}
