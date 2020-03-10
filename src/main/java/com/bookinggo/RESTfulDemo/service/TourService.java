package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TourService {
    private TourRepository tourRepository;

    public List<Tour> lookupAllTours()  {
        return tourRepository.findAll();
    }

    public Optional lookupToursById(int id)  {
        log.info("lookupTours - packageId: {}", id);
        Optional<Tour> tours = tourRepository.findById(id);

        return tours;
    }
}
