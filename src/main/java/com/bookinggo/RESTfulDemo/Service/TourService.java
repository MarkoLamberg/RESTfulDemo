package com.bookinggo.RESTfulDemo.Service;

import com.bookinggo.RESTfulDemo.entity.Region;
import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourPackage;
import com.bookinggo.RESTfulDemo.repository.TourPackageRepository;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TourService {
    private TourPackageRepository tourPackageRepository;
    private TourRepository tourRepository;

    public Tour createTour(String title, Integer price, String duration, String tourPackageName, Region region){
        TourPackage tourPackage = tourPackageRepository.findByName(tourPackageName).orElseThrow(()->
                new RuntimeException("Tour package does not exist: " + tourPackageName));

        return tourRepository.save(new Tour(title, price, duration, tourPackage, region));
    }

    public Iterable<Tour> lookup() {
        return tourRepository.findAll();
    }

    public long total() {
        return tourRepository.count();
    }
}
