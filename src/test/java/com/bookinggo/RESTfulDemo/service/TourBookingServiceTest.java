
package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.repository.TourBookingRepository;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourBookingServiceTest {
    private static final int CUSTOMER_ID = 123;
    private static final int TOUR_ID = 123;
    private static final int TOUR_BOOKING_ID = 123;
    private static final String DATE = "20-03-2020";
    private static final String LOCATION = "Hotel Ibis";
    private static final int PARTISIPANTS = 1;

    @Mock
    private TourRepository tourRepositoryMock;
    @Mock
    private TourBookingRepository tourBookingRepositoryMock;

    @InjectMocks
    private TourBookingService service;

    @Mock
    private Tour tourMock;

    @Mock
    private TourBooking tourBookingMock;

    @BeforeEach
    public void setReturnValuesOfMockMethods() {
/*        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tourMock));
        when(tourMock.getId()).thenReturn(TOUR_ID);
        when(tourBookingRepositoryMock.findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID)).thenReturn(Optional.of(tourBookingMock));
        when(tourBookingRepositoryMock.findByTourId(TOUR_ID)).thenReturn(Arrays.asList(tourBookingMock));*/

    }

    @Test
    public void createNew_TourWithIdNonExisting_FailsToCreate() {
        service.createNew(TOUR_ID, CUSTOMER_ID, DATE, LOCATION, PARTISIPANTS);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verify(tourRepositoryMock, times(0)).save(any());
    }

    @Test
    public void lookupBookingsAfter_TourWithIdNonExisting_NothingToReturn() {
        List<TourBooking> bookings = service.lookupBookingsAfter(TOUR_ID);
        verify(tourBookingRepositoryMock, times(1)).findAllByCustomerIdAfter(TOUR_ID);
        assertEquals(bookings.size(), 0);
    }

    @Test
    public void lookupBookingById_TourBookingWithIdNonExisting_NothingToReturn() {
        List<TourBooking> bookings = service.lookupBookings(TOUR_BOOKING_ID);
        verify(tourBookingRepositoryMock, times(1)).findByTourId(TOUR_BOOKING_ID);
        assertEquals(bookings.size(), 0);
    }

    @Test
    public void lookupAllBookings_NoBookingsAvailable_NothingToReturn() {
        List<TourBooking> bookings = service.lookupAllBookings();
        verify(tourBookingRepositoryMock, times(1)).findAll();
        assertEquals(bookings.size(), 0);
    }

    @Test
    public void update_BookingNonExisting_NothingToUpdate() throws NoSuchElementException {
        TourBooking booking = service.update(TOUR_ID, CUSTOMER_ID, DATE, LOCATION);

        verify(tourBookingRepositoryMock, times(1)).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verify(tourBookingMock, times(0)).setDate(DATE);
        verify(tourBookingMock, times(0)).setPickupLocation(LOCATION);
        verify(tourBookingRepositoryMock, times(1)).save(null);
        assertEquals(booking, null);
    }

    @Test
    public void updateSome_BookingNonExisting_NothingToUpdate() throws NoSuchElementException {
        TourBooking booking = service.updateSome(TOUR_ID, CUSTOMER_ID, DATE, LOCATION);

        verify(tourBookingRepositoryMock, times(1)).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verify(tourBookingMock, times(0)).setDate(DATE);
        verify(tourBookingMock, times(0)).setPickupLocation(LOCATION);
        verify(tourBookingRepositoryMock, times(1)).save(null);
        assertEquals(booking, null);
    }

    @Test()
    public void deleteByTourIdAndCustomerId_BookingWithTourIdAndCustomerIdNonExisting_NothingToDelete() throws NoSuchElementException{
        service.delete(TOUR_ID, CUSTOMER_ID);

        verify(tourBookingRepositoryMock, times(1)).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verify(tourBookingRepositoryMock, times(1)).delete(any());
    }

    @Test
    public void deleteByCustomerId_BookingWithCustomerIdNonExisting_NothingToDelete() {
        service.delete(CUSTOMER_ID);

        verify(tourBookingRepositoryMock, times(1)).findByCustomerId(CUSTOMER_ID);
        verify(tourBookingRepositoryMock, times(0)).delete(any());
    }

    @Test
    public void deleteAll_NoBookingsAvailable_NothingToDelete() {
        service.deleteAll();
        verify(tourBookingRepositoryMock, times(1)).deleteAll();
    }
}

