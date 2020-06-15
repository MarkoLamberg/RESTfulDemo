package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourPackage;
import com.bookinggo.RESTfulDemo.repository.TourPackageRepository;
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

    private final TourPackageRepository tourPackageRepository;

    @Override
    public Optional<Tour> createTour(String tourPackageCode, String title, String duration, int price) {
        log.info("createTour - tourPackageCode: {}, title: {}, duration: {}, price: {}", tourPackageCode, title, duration, price);
        Optional<TourPackage> tourPackage = tourPackageRepository.getTourPackageByCode(tourPackageCode);

        if (tourPackage.isPresent()) {
            Tour tour = Tour.builder()
                    .tourPackage(tourPackage.get())
                    .title(title)
                    .duration(duration)
                    .price(price)
                    .build();

            return Optional.of(tourRepository.save(tour));
        }

        return Optional.empty();
    }

    @Override
    public List<Tour> lookupAllTours() {
        return tourRepository.findAll();
    }

    @Override
    public Optional<Tour> lookupTourById(int id) {
        log.info("lookupTourById - id: {}", id);
        Optional<Tour> tour = tourRepository.findById(id);

        return tour;
    }

    @Override
    public List<Tour> lookupToursByLocation(String location) {
        log.info("lookupToursByLocation - location: {}", location);
        List<Tour> tours = tourRepository.findAll();
        List<Tour> toursByLocation = tours.stream()
                .filter(tour -> tour.getTourPackage().getLocation().equalsIgnoreCase(location))
                .collect(Collectors.toList());

        return toursByLocation;
    }
}
