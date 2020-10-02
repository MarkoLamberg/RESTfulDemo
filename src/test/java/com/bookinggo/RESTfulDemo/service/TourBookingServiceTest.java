package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Customer;
import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.exception.TourBookingServiceException;
import com.bookinggo.RESTfulDemo.repository.CustomerRepository;
import com.bookinggo.RESTfulDemo.repository.TourBookingRepository;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TourBookingServiceTest {

    private static final int CUSTOMER_ID = 123;

    private static final int TOUR_ID = 234;

    private static final LocalDateTime PICKUP_DATE_TIME = LocalDateTime.now();

    private static final String PICKUP_LOCATION = "Hotel Ibis";

    private static final int PARTICIPANTS = 1;

    @MockBean
    private TourRepository tourRepositoryMock;

    @MockBean
    private TourBookingRepository tourBookingRepositoryMock;

    @MockBean
    private CustomerRepository customerRepositoryMock;

    @Autowired
    private TourBookingService tourBookingService;

    @Value("${retry.attempts}")
    private int retryAttempts;

    @Test
    public void shouldNotCreateBooking_whenCreateBooking_givenTourIdDoesNotExist() throws TourBookingServiceException {
        try {
            tourBookingService.createBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS);
        } catch (TourBookingServiceException e) {
        }

        verify(tourRepositoryMock, times(retryAttempts)).findById(TOUR_ID);
        verify(customerRepositoryMock, never()).findById(CUSTOMER_ID);
        verify(tourRepositoryMock, never()).save(any());
    }

    @Test
    public void shouldNotCreateBooking_whenCreateBooking_givenCustomerDoesNotExist() throws TourBookingServiceException {
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(buildTour()));

        try {
            tourBookingService.createBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS);
        } catch (TourBookingServiceException e) {
        }

        verify(tourRepositoryMock, times(retryAttempts)).findById(TOUR_ID);
        verify(customerRepositoryMock, times(retryAttempts)).findById(CUSTOMER_ID);
        verify(tourBookingRepositoryMock, never()).save(any());
    }

    @Test
    public void shouldCreateABooking_whenCreateBooking_givenTourIdDoesExist() throws TourBookingServiceException {
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(buildTour()));
        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(buildCustomer()));
        when(tourBookingRepositoryMock.save(any())).thenReturn(buildTourBooking());

        Optional<TourBooking> booking = tourBookingService.createBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS);

        verify(tourRepositoryMock).findById(TOUR_ID);
        verify(customerRepositoryMock).findById(CUSTOMER_ID);
        verify(tourBookingRepositoryMock).save(any());
        assertThat(booking).isPresent();
    }

    @Test
    public void shouldNotReturnAnyBookings_whenGetTourBookings_givenNoBookingsWithIdExist() {
        try {
            tourBookingService.getBookingsByTourId(TOUR_ID);
        } catch (TourBookingServiceException e) {
        }

        verify(tourBookingRepositoryMock).findByTourId(TOUR_ID);
        assertThatExceptionOfType(TourBookingServiceException.class).isThrownBy(() -> tourBookingService.getBookingsByTourId(TOUR_ID));
    }

    @Test
    public void shouldReturnABooking_whenGetTourBookings_givenBookingWithIdExist() {
        when(tourBookingRepositoryMock.findByTourId(TOUR_ID)).thenReturn(of(buildTourBooking()));

        List<TourBooking> bookings = tourBookingService.getBookingsByTourId(TOUR_ID);

        verify(tourBookingRepositoryMock).findByTourId(TOUR_ID);
        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void shouldNotReturnAnyBookings_whenGetAllBookings_givenNoBookingsExist() {
        List<TourBooking> bookings = tourBookingService.getAllBookings();

        verify(tourBookingRepositoryMock).findAll();
        assertThat(bookings.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnABookings_whenGetAllBookings_givenBookingsExists() {
        when(tourBookingRepositoryMock.findAll()).thenReturn(of(buildTourBooking()));

        List<TourBooking> bookings = tourBookingService.getAllBookings();

        verify(tourBookingRepositoryMock).findAll();
        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void shouldNotUpdateBooking_whenUpdateBooking_givenBookingWithTourIdAndCustomerIdNonExisting() {
        try {
            tourBookingService.updateBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS);
        } catch (TourBookingServiceException e) {
        }

        verify(customerRepositoryMock).findById(CUSTOMER_ID);
        verifyNoInteractions(tourBookingRepositoryMock);
        assertThatExceptionOfType(TourBookingServiceException.class).isThrownBy(() -> tourBookingService.updateBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS));
    }

    @Test
    public void shouldUpdateABooking_whenUpdateBookingWithThreeFields_givenBookingWithTourIdAndCustomerIdExists() {
        TourBooking tourBooking = spy(buildTourBooking());
        Customer customer = buildCustomer();

        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(tourBookingRepositoryMock.findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID)).thenReturn(of(tourBooking));
        when(tourBookingRepositoryMock.saveAndFlush(any())).thenReturn(buildTourBooking(customer));

        Optional<TourBooking> updatedBooking = tourBookingService.updateBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS);

        verify(customerRepositoryMock).findById(CUSTOMER_ID);
        verify(tourBookingRepositoryMock).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verify(tourBooking).setPickupDateTime(PICKUP_DATE_TIME);
        verify(tourBooking).setPickupLocation(PICKUP_LOCATION);
        verify(tourBooking).setParticipants(PARTICIPANTS);
        verify(tourBookingRepositoryMock).saveAndFlush(tourBooking);
        assertThat(updatedBooking).isPresent();
    }

    @Test
    public void shouldUpdateABooking_whenUpdateBookingWithTwoFields_givenBookingWithTourIdAndCustomerIdExists() {
        TourBooking tourBooking = spy(buildTourBooking());
        Customer customer = buildCustomer();

        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(tourBookingRepositoryMock.findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID)).thenReturn(of(tourBooking));
        when(tourBookingRepositoryMock.saveAndFlush(any())).thenReturn(buildTourBooking(customer));

        Optional<TourBooking> updatedBooking = tourBookingService.updateBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, null);

        verify(customerRepositoryMock).findById(CUSTOMER_ID);
        verify(tourBookingRepositoryMock).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verify(tourBooking).setPickupDateTime(PICKUP_DATE_TIME);
        verify(tourBooking).setPickupLocation(PICKUP_LOCATION);
        verify(tourBooking, times(0)).setParticipants(PARTICIPANTS);
        verify(tourBookingRepositoryMock).saveAndFlush(tourBooking);
        assertThat(updatedBooking).isPresent();
    }

    @Test
    public void shouldUpdateABooking_whenUpdateBookingWithOneField_givenBookingWithTourIdAndCustomerIdExists() {
        TourBooking tourBooking = spy(buildTourBooking());
        Customer customer = buildCustomer();

        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(tourBookingRepositoryMock.findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID)).thenReturn(of(tourBooking));
        when(tourBookingRepositoryMock.saveAndFlush(any())).thenReturn(buildTourBooking(customer));

        Optional<TourBooking> updatedBooking = tourBookingService.updateBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, null, null);

        verify(customerRepositoryMock).findById(CUSTOMER_ID);
        verify(tourBookingRepositoryMock).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verify(tourBooking).setPickupDateTime(PICKUP_DATE_TIME);
        verify(tourBooking, times(0)).setPickupLocation(PICKUP_LOCATION);
        verify(tourBooking, times(0)).setParticipants(PARTICIPANTS);
        verify(tourBookingRepositoryMock).saveAndFlush(tourBooking);
        assertThat(updatedBooking).isPresent();
    }

    @Test
    public void shouldUpdateABooking_whenUpdateBookingWithZeroFields_givenBookingWithTourIdAndCustomerIdExists() {
        TourBooking tourBooking = spy(buildTourBooking());
        Customer customer = buildCustomer();

        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(tourBookingRepositoryMock.findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID)).thenReturn(of(tourBooking));
        when(tourBookingRepositoryMock.saveAndFlush(any())).thenReturn(buildTourBooking(customer));

        Optional<TourBooking> updatedBooking = tourBookingService.updateBooking(TOUR_ID, CUSTOMER_ID, null, null, null);

        verify(customerRepositoryMock).findById(CUSTOMER_ID);
        verify(tourBookingRepositoryMock).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verifyNoMoreInteractions(tourBooking);
        verify(tourBookingRepositoryMock).saveAndFlush(tourBooking);
        assertThat(updatedBooking).isPresent();
    }

    @Test
    public void shouldNotDeleteAnyBookings_whenDeleteAllBookingsWithTourId_givenBookingsWithTourIdNonExisting() {
        List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithTourId(TOUR_ID);

        verify(tourBookingRepositoryMock).findByTourId(TOUR_ID);
        verify(tourBookingRepositoryMock, times(0)).delete(any());
        assertThat(bookings.size()).isEqualTo(0);
    }

    @Test
    public void shouldDeleteABooking_whenDeleteAllBookingsWithTourId_givenBookingsWithTourIdExists() {
        when(tourBookingRepositoryMock.findByTourId(TOUR_ID)).thenReturn(of(buildTourBooking()));

        List<TourBooking> bookings = tourBookingService.deleteAllBookingsWithTourId(TOUR_ID);

        verify(tourBookingRepositoryMock).findByTourId(TOUR_ID);
        verify(tourBookingRepositoryMock).delete(any());
        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void shouldNotDeleteAnyBookings_whenDeleteAllBookingsWithTourIdAndCustomerId_givenBookingsWithTourIdAndCustomerIdNonExisting() {
        try {
            tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        } catch (TourBookingServiceException e) {
        }

        verify(tourBookingRepositoryMock, times(0)).findByTourId(TOUR_ID);
        verify(tourBookingRepositoryMock, times(0)).delete(any());
        assertThatExceptionOfType(TourBookingServiceException.class).isThrownBy(() -> tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID));
    }

    @Test
    public void shouldDeleteABooking_whenDeleteAllBookingsWithTourIdAndCustomerId_givenBookingsWithTourIdAndCustomerIdExists() {
        Customer customer = buildCustomer();
        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(tourBookingRepositoryMock.findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID)).thenReturn(of(buildTourBooking(customer)));

        Optional<List<TourBooking>> bookings = tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);

        verify(tourBookingRepositoryMock).findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);
        verify(tourBookingRepositoryMock).delete(any());
        assertThat(bookings.get().size()).isEqualTo(1);
    }

    @Test
    public void shouldNotDeleteAnyBookings_whenDeleteAllBookingsWithCustomerId_givenBookingsWithCustomerIdNonExisting() {
        try {
            tourBookingService.deleteAllBookingsWithCustomerId(CUSTOMER_ID);
        } catch (TourBookingServiceException e) {
        }

        verify(tourBookingRepositoryMock).findByCustomerId(CUSTOMER_ID);
        verify(tourBookingRepositoryMock, times(0)).delete(any());
        assertThatExceptionOfType(TourBookingServiceException.class).isThrownBy(() -> tourBookingService.deleteAllBookingsWithCustomerId(CUSTOMER_ID));
    }

    @Test
    public void shouldDeleteABooking_whenDeleteAllBookingsWithCustomerId_givenBookingsWithCustomerIdExists() {
        Customer customer = buildCustomer();
        when(customerRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(tourBookingRepositoryMock.findByCustomerId(CUSTOMER_ID)).thenReturn(of(buildTourBooking(customer)));
        Optional<List<TourBooking>> bookings = tourBookingService.deleteAllBookingsWithCustomerId(CUSTOMER_ID);

        verify(tourBookingRepositoryMock).findByCustomerId(CUSTOMER_ID);
        verify(tourBookingRepositoryMock).delete(any());
        assertThat(bookings.get().size()).isEqualTo(1);
    }

    @Test
    public void shouldNotDeleteAnyBookings_whenDeleteAllBookings_givenNoBookings() {
        try {
            tourBookingService.deleteAllBookings();
        } catch (TourBookingServiceException e) {
        }

        verify(tourBookingRepositoryMock).findAll();
        verify(tourBookingRepositoryMock, times(0)).deleteAll();
        assertThatExceptionOfType(TourBookingServiceException.class).isThrownBy(() -> tourBookingService.deleteAllBookings());
    }

    @Test
    public void shouldDeleteABooking_whenDeleteAllBookings_givenOneBooking() {
        when(tourBookingRepositoryMock.findAll()).thenReturn(of(buildTourBooking()));

        List<TourBooking> tourBookings = tourBookingService.deleteAllBookings();
        verify(tourBookingRepositoryMock).findAll();
        verify(tourBookingRepositoryMock).deleteAll();
        assertThat(tourBookings.size()).isEqualTo(1);
    }

    private TourBooking buildTourBooking() {
        return TourBooking
                .builder()
                .build();
    }

    private TourBooking buildTourBooking(Customer customer) {
        return TourBooking
                .builder()
                .customer(customer)
                .pickupLocation(PICKUP_LOCATION)
                .pickupDateTime(PICKUP_DATE_TIME)
                .build();
    }

    private Customer buildCustomer() {
        return Customer
                .builder()
                .id(CUSTOMER_ID)
                .build();
    }

    private Tour buildTour() {
        return Tour
                .builder()
                .id(TOUR_ID)
                .build();
    }
}

