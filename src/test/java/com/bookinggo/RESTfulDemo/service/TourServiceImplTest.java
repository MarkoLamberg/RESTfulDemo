package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TourServiceImplTest {

    private static final int TOUR_ID = 234;

    @Autowired
    private TourServiceImpl service;

    @MockBean
    private TourRepository tourRepositoryMock;

    @Test
    public void lookupAllTours_NoToursAvailable_NothingToReturn() {
        List<Tour> tours = service.lookupAllTours();
        verify(tourRepositoryMock, times(1)).findAll();
        assertEquals(0, tours.size());
    }

    @Test
    public void lookupTourById_TourWithIdNonExisting_NothingToReturn() {
        Optional<Tour> tour = service.lookupTourById(TOUR_ID);
        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        assertEquals(Optional.empty(), tour);
    }
}
