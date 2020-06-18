package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.repository.TourBookingRepository;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TourBookingServiceTest {

    private static final int CUSTOMER_ID = 123;

    private static final int TOUR_ID = 234;

    private static final LocalDateTime PICKUP_DATE_TIME = LocalDateTime.of(2020, 03, 20, 12, 00);

    private static final String PICKUP_LOCATION = "Hotel Ibis";

    private static final int PARTICIPANTS = 1;

    @MockBean
    private TourRepository tourRepositoryMock;

    @MockBean
    private TourBookingRepository tourBookingRepositoryMock;

    @Autowired
    private TourBookingService tourBookingService;

    @Mock
    private TourBooking tourBookingMock;

    @Test
    public void shouldNotCreateBooking_whenCreate_givenTourIdDoesNotExist() {
        tourBookingService.createBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verify(tourRepositoryMock, times(0)).save(any());
    }

    @Test
    public void shouldNotReturnAnyBookings_whenLookupTourBookings_givenNoBookingsWithIdExist() {
        List<TourBooking> bookings = tourBookingService.lookupTourBookings(TOUR_ID);

        verify(tourBookingRepositoryMock, times(1)).findByTourId(TOUR_ID);
        assertThat(bookings.size()).isEqualTo(0);
    }

    @Test
    public void shouldNotReturnAnyBookings_whenLookupAllBookings_givenNoBookingsExist() {
        List<TourBooking> bookings = tourBookingService.lookupAllBookings();

        verify(tourBookingRepositoryMock, times(1)).findAll();
        assertThat(bookings.size()).isEqualTo(0);
    }

    @Test
    public void shouldNotUpdateBooking_whenUpdateBooking_givenBookingWithTourIdAndCustomerIdNonExisting() {
        TourBooking booking = tourBookingService.updateBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS);

        verify(tourBookingRepositoryMock, times(1)).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verify(tourBookingMock, times(0)).setPickupDateTime(PICKUP_DATE_TIME);
        verify(tourBookingMock, times(0)).setPickupLocation(PICKUP_LOCATION);
        verify(tourBookingRepositoryMock, times(0)).saveAndFlush(null);
        assertThat(booking).isNull();
    }

    @Test
    public void shouldNotDeleteAnyBookings_whenDeleteAllBookingsWithTourId_givenBookingsWithTourIdNonExisting() {
        tourBookingService.deleteAllBookingsWithTourId(TOUR_ID);

        verify(tourBookingRepositoryMock, times(1)).findByTourId(TOUR_ID);
        verify(tourBookingRepositoryMock, times(0)).delete(any());
    }

    @Test
    public void shouldNotDeleteAnyBookings_whenDeleteAllBookingsWithTourIdAndCustomerId_givenBookingsWithTourIdAndCustomerIdNonExisting() {
        tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);

        verify(tourBookingRepositoryMock, times(1)).findByTourId(TOUR_ID);
        verify(tourBookingRepositoryMock, times(0)).delete(any());
    }

    @Test
    public void shouldNotDeleteAnyBookings_whenDeleteAllBookingsWithCustomerId_givenBookingsWithCustomerIdNonExisting() {
        tourBookingService.deleteAllBookingsWithCustomerId(CUSTOMER_ID);

        verify(tourBookingRepositoryMock, times(1)).findByCustomerId(CUSTOMER_ID);
        verify(tourBookingRepositoryMock, times(0)).delete(any());
    }

    @Test
    public void shouldNotDeleteAnyBookings_whenDeleteAllBookings_givenNoBookings() {
        tourBookingService.deleteAllBookings();
        verify(tourBookingRepositoryMock, times(1)).deleteAll();
    }
}

