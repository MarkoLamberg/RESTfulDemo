package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
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

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TourBookingServiceImplTest {

    private static final int CUSTOMER_ID = 123;
    private static final int TOUR_ID = 234;
    private static final int TOUR_BOOKING_ID = 345;
    private static final String DATE = "20-03-2020";
    private static final String LOCATION = "Hotel Ibis";
    private static final int PARTICIPANTS = 1;

    @MockBean
    private TourRepository tourRepositoryMock;

    @MockBean
    private TourBookingRepository tourBookingRepositoryMock;

    @Autowired
    private TourBookingServiceImpl service;

    @Mock
    private Tour tourMock;

    @Mock
    private TourBooking tourBookingMock;

    @Test
    public void shouldNotCreateBooking_whenCreate_givenTourIdDoesNotExist() {
        service.createNew(TOUR_ID, CUSTOMER_ID, DATE, LOCATION, PARTICIPANTS);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verify(tourRepositoryMock, times(0)).save(any());
    }

    @Test
    public void shouldNotReturnAnyBookings_whenLookupTourBookings_givenNoBookingsWithIdExist() {
        List<TourBooking> bookings = service.lookupTourBookings(TOUR_ID);
        verify(tourBookingRepositoryMock, times(1)).findByTourId(TOUR_ID);
        assertEquals(bookings.size(), 0);
    }

    @Test
    public void shouldNotReturnAnyBookings_whenLookupAllBookings_givenNoBookingsExist() {
        List<TourBooking> bookings = service.lookupAllBookings();
        verify(tourBookingRepositoryMock, times(1)).findAll();
        assertEquals(bookings.size(), 0);
    }

    @Test
    public void shouldNotUpdateBooking_whenUpdate_givenBookingWithTourIdAndCustomerIdNonExisting() throws NoSuchElementException {
        TourBooking booking = service.update(TOUR_ID, CUSTOMER_ID, DATE, LOCATION, PARTICIPANTS);

        verify(tourBookingRepositoryMock, times(1)).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verify(tourBookingMock, times(0)).setDate(DATE);
        verify(tourBookingMock, times(0)).setPickupLocation(LOCATION);
        verify(tourBookingRepositoryMock, times(1)).saveAndFlush(null);
        assertEquals(booking, null);
    }

    @Test
    public void shouldNotUpdateBooking_whenUpdateSome_givenBookingWithTourIdAndCustomerIdNonExisting() throws NoSuchElementException {
        TourBooking booking = service.updateSome(TOUR_ID, CUSTOMER_ID, DATE, LOCATION, PARTICIPANTS);

        verify(tourBookingRepositoryMock, times(1)).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verify(tourBookingMock, times(0)).setDate(DATE);
        verify(tourBookingMock, times(0)).setPickupLocation(LOCATION);
        verify(tourBookingRepositoryMock, times(1)).saveAndFlush(null);
        assertEquals(booking, null);
    }

    @Test()
    public void shouldNotDeleteAnyBookings_whenDeleteAllBookingsWithTourIdAndCustomerId_givenBookingsWithTourIdAndCustomerIdNonExisting() throws NoSuchElementException {
        service.deleteAllBookingsWithTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);

        verify(tourBookingRepositoryMock, times(1)).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verify(tourBookingRepositoryMock, times(1)).delete(any());
    }

    @Test
    public void shouldNotDeleteAnyBookings_whenDeleteAllBookingsWithCustomerId_givenBookingsWithCustomerIdNonExisting() {
        service.deleteAllBookingsWithCustomerId(CUSTOMER_ID);

        verify(tourBookingRepositoryMock, times(1)).findByCustomerId(CUSTOMER_ID);
        verify(tourBookingRepositoryMock, times(0)).delete(any());
    }

    @Test
    public void shouldNotDeleteAnyBookings_whenDeleteAllBookings_givenNoBookings() {
        service.deleteAllBookings();
        verify(tourBookingRepositoryMock, times(1)).deleteAll();
    }
}

