package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourPackage;
import com.bookinggo.RESTfulDemo.exception.TourServiceException;
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
        final Optional<TourPackage> tourPackage = tourPackageRepository.getTourPackageByCode(tourPackageCode);

        if (tourPackage.isPresent()) {
            final Tour tour = Tour.builder()
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
    public Optional<Tour> updateTour(int tourId, String tourPackageCode, String title, String duration, Integer price) {
        log.info("updateTour - tourId: {}, tourPackageCode: {}, title: {}, duration: {}, price: {}", tourId, tourPackageCode, title, duration, price);
        final Optional<Tour> tour = tourRepository.findById(tourId);

        if (tour.isPresent()) {
            if (tourPackageCode != null) {
                final Optional<TourPackage> tourPackage = tourPackageRepository.getTourPackageByCode(tourPackageCode);

                if (tourPackage.isPresent()) {
                    tour.get().setTourPackage(tourPackage.get());
                } else {
                    throw new TourServiceException("Can't update tour. The Tour Package doesn't exist. Provide correct Tour Package Code.", null);
                }
            }

            if (title != null) {
                tour.get().setTitle(title);
            }

            if (duration != null) {
                tour.get().setDuration(duration);
            }

            if (price != null) {
                tour.get().setPrice(price);
            }

            return Optional.of(tourRepository.saveAndFlush(tour.get()));
        }

        throw new TourServiceException("Can't update tour. Tour doesn't exist. Provide correct Tour Id.", null);
    }

    @Override
    public List<Tour> getAllTours() {
        log.info("getAllTours");
        return tourRepository.findAll();
    }

    @Override
    public Optional<Tour> getTourById(int tourId) {
        log.info("getTourById - id: {}", tourId);
        final Optional<Tour> tour = tourRepository.findById(tourId);

        if (tour.isPresent()) {
            return tour;
        }

        throw new TourServiceException("Can't get tour by id. Tour doesn't exist. Provide correct Tour Id.", null);
    }

    @Override
    public List<Tour> getToursByLocation(String location) {
        log.info("getToursByLocation - location: {}", location);
        final List<Tour> tours = tourRepository.findAll();
        final List<Tour> toursByLocation = tours.stream()
                .filter(tour -> tour.getTourPackage().getLocation().equalsIgnoreCase(location))
                .collect(Collectors.toList());

        if (toursByLocation.size() > 0) {
            return toursByLocation;
        }

        throw new TourServiceException("Can't get tours by location. Tour Location doesn't exist. Provide correct Tour Location.", null);
    }

    @Override
    public Optional<Tour> getTourByTourPackageCodeAndTitle(String tourPackageCode, String title) {
        log.info("getTourByTourPackageCodeAndTitle - tourPackageCode: {}, title: {}", tourPackageCode, title);
        final Optional<TourPackage> tourPackage = tourPackageRepository.getTourPackageByCode(tourPackageCode);

        if (tourPackage.isPresent()) {
            return tourRepository.findTourByTourPackageAndTitle(tourPackage.get(), title);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Tour> deleteTourById(int tourId) {
        log.info("deleteTourById - tourId: {}", tourId);
        final Optional<Tour> tour = tourRepository.findById(tourId);

        if (tour.isPresent()) {
            tourRepository.deleteById(tourId);

            return tour;
        }

        throw new TourServiceException("Can't delete tour. Tour doesn't exist. Provide correct Tour Id.", null);
    }
}
