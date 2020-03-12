package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
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
}
