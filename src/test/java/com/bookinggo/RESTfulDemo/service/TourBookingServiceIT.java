package com.bookinggo.RestfulDemo.service;

import com.bookinggo.RestfulDemo.ServiceTests;
import com.bookinggo.RestfulDemo.entity.TourBooking;
import com.bookinggo.RestfulDemo.exception.TourBookingServiceException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
public class TourBookingServiceIT extends AbstractRestfulDemoIT implements ServiceTests {

    private static final int CUSTOMER_ID = 4;
    private static final int TOUR_ID = 1;
    private static final int BOOKING_ID = 3;
    private static final int NON_EXISTING_BOOKING_ID = 5;
    private static final int PARTICIPANTS = 1;
    private static final int ORIGINAL_PARTICIPANTS = 2;
    private static final LocalDateTime PICKUP_DATE_TIME = LocalDateTime.of(2020, 3, 20, 12, 0);
    private static final LocalDateTime ORIGINAL_PICKUP_DATE_TIME = LocalDateTime.of(2019, 11, 18, 6, 0);
    private static final String PICKUP_LOCATION = "Hotel Ibis";
    private static final String ORIGINAL_PICKUP_LOCATION = "Hotel Intercontinental";

    @Autowired
    private TourBookingService tourBookingService;

    @Sql
    @Test
    public void shouldCreateABooking_whenCreateBooking_givenValidBooking() throws TourBookingServiceException {
        List<TourBooking> bookingsBefore = tourBookingService.getAllBookings();
        assertThat(bookingsBefore.size()).isEqualTo(0);

        tourBookingService.createBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS);

        List<TourBooking> bookingsAfter = tourBookingService.getAllBookings();
        TourBooking booking = bookingsAfter.get(0);

        assertAll(
                () -> assertThat(bookingsAfter.size()).isEqualTo(1),
                () -> assertThat(booking.getTour().getId().intValue()).isEqualTo(TOUR_ID),
                () -> assertThat(booking.getCustomer().getId().intValue()).isEqualTo(CUSTOMER_ID),
                () -> assertThat(booking.getPickupLocation()).isEqualTo(PICKUP_LOCATION),
                () -> assertThat(booking.getParticipants().intValue()).isEqualTo(PARTICIPANTS));
    }

    @Sql
    @Test
    public void shouldReturnBooking_whenGetTourBookings_givenBookingWithTourIdExists() {
        List<TourBooking> bookings = tourBookingService.getBookingsByTourId(TOUR_ID);

        assertAll(
                () -> assertThat(bookings.size()).isEqualTo(1),
                () -> assertThat(bookings.get(0).getTour().getId().intValue()).isEqualTo(TOUR_ID));
    }

    @Sql
    @Test
    public void shouldReturnTwoBookings_whenGetAllBookings_givenBookingsExist() {
        List<TourBooking> bookings = tourBookingService.getAllBookings();
        assertThat(bookings.size()).isEqualTo(2);
    }

    @Sql
    @Test
    public void shouldUpdateBooking_whenUpdateBooking_givenBookingWithTourIdAndCustomerIdExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.getBookingsByTourId(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(filteredBookingsBefore.get(0).getPickupDateTime()).isNotEqualTo(PICKUP_DATE_TIME),
                () -> assertThat(filteredBookingsBefore.get(0).getPickupLocation()).isNotEqualTo(PICKUP_LOCATION));

        tourBookingService.updateBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS);

        List<TourBooking> filteredBookingsAfter = tourBookingService.getBookingsByTourId(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(filteredBookingsAfter.get(0).getPickupDateTime()).isEqualTo(PICKUP_DATE_TIME),
                () -> assertThat(filteredBookingsAfter.get(0).getPickupLocation()).isEqualTo(PICKUP_LOCATION));
    }

    @Sql
    @ParameterizedTest
    @MethodSource("dateTimeAndLocationAndParticipantsAndBookingProvider")
    public void parameterized_shouldUpdateBooking_whenUpdateBooking_givenBookingWithTourIdAndCustomerIdExists(LocalDateTime dateTime, String location, Integer participants, TourBooking updatedBooking) {
        tourBookingService.updateBooking(TOUR_ID, CUSTOMER_ID, dateTime, location, participants);

        List<TourBooking> filteredBookingsAfter = tourBookingService.getBookingsByTourId(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(filteredBookingsAfter.get(0).getPickupDateTime()).isEqualTo(updatedBooking.getPickupDateTime()),
                () -> assertThat(filteredBookingsAfter.get(0).getPickupLocation()).isEqualTo(updatedBooking.getPickupLocation()),
                () -> assertThat(filteredBookingsAfter.get(0).getParticipants()).isEqualTo(updatedBooking.getParticipants()));
    }

    @Sql
    @Test
    public void shouldNotUpdateBooking_whenUpdateBooking_givenMoreThanOneBookingsWithTourIdAndCustomerIdExist() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.getBookingsByTourId(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(filteredBookingsBefore.size()).isNotEqualTo(1),
                () -> assertThrows(TourBookingServiceException.class,
                        () -> tourBookingService.updateBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS)));
    }

    @Sql
    @Test
    public void shouldUpdateBooking_whenUpdateBookingSome_givenBookingWithTourIdAndCustomerExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.getBookingsByTourId(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(filteredBookingsBefore.get(0).getPickupDateTime()).isNotEqualTo(PICKUP_DATE_TIME),
                () -> assertThat(filteredBookingsBefore.get(0).getPickupLocation()).isNotEqualTo(PICKUP_LOCATION));

        tourBookingService.updateBooking(TOUR_ID, CUSTOMER_ID, PICKUP_DATE_TIME, PICKUP_LOCATION, null);

        List<TourBooking> filteredBookingsAfter = tourBookingService.getBookingsByTourId(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(filteredBookingsAfter.get(0).getPickupDateTime()).isEqualTo(PICKUP_DATE_TIME),
                () -> assertThat(filteredBookingsAfter.get(0).getPickupLocation()).isEqualTo(PICKUP_LOCATION),
                () -> assertThat(filteredBookingsAfter.get(0).getPickupLocation()).isNotEqualTo(PARTICIPANTS));
    }

    @Sql
    @Test
    public void shouldDeleteBooking_whenDeleteAllBookingsWithTourId_givenBookingWithTourIdExists() {
        List<TourBooking> bookingsBefore = tourBookingService.getBookingsByTourId(TOUR_ID);
        assertThat(bookingsBefore.size()).isEqualTo(1);

        List<TourBooking> deletedBookings = tourBookingService.deleteAllBookingsWithTourId(TOUR_ID);

        assertAll(
                () -> assertThat(deletedBookings.size()).isEqualTo(1),
                () -> assertThrows(TourBookingServiceException.class,
                        () -> tourBookingService.getBookingsByTourId(TOUR_ID)));
    }

    @Sql
    @Test
    public void shouldDeleteBooking_whenDeleteAllBookingsWithTourIdAndCustomerId_givenBookingWithTourIdAndCustomerIdExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.getBookingsByTourId(TOUR_ID)
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsBefore.size()).isEqualTo(1);

        List<TourBooking> deletedBookings = tourBookingService.deleteAllBookingsWithTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID);

        assertAll(
                () -> assertThat(deletedBookings.size()).isEqualTo(1),
                () -> assertThrows(TourBookingServiceException.class,
                        () -> tourBookingService.getBookingsByTourId(TOUR_ID)));
    }

    @Sql
    @Test
    public void shouldDeleteAllBookings_whenDeleteAllBookingsWithCustomerId_givenBookingsWithCustomerIdExists() {
        List<TourBooking> filteredBookingsBefore = tourBookingService.getAllBookings()
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsBefore.size()).isEqualTo(2);

        List<TourBooking> deletedBookings = tourBookingService.deleteAllBookingsWithCustomerId(CUSTOMER_ID);
        assertThat(deletedBookings.size()).isEqualTo(2);

        List<TourBooking> filteredBookingsAfter = tourBookingService.getAllBookings()
                .stream()
                .filter(booking -> booking.getCustomer().getId().equals(CUSTOMER_ID))
                .collect(Collectors.toList());

        assertThat(filteredBookingsAfter.size()).isEqualTo(0);
    }

    @Test
    @Sql
    public void shouldDeleteAllBookings_whenDeleteAllBookings_givenBookingsExists() {
        List<TourBooking> bookingsBefore = tourBookingService.getAllBookings();
        assertThat(bookingsBefore.size()).isEqualTo(3);

        List<TourBooking> deletedBookings = tourBookingService.deleteAllBookings();
        assertThat(deletedBookings.size()).isEqualTo(3);

        List<TourBooking> bookingsAfter = tourBookingService.getAllBookings();
        assertThat(bookingsAfter.size()).isEqualTo(0);
    }

    @Test
    @Sql("classpath:com/bookinggo/RESTfulDemo/service/TourBookingServiceIT.shouldDeleteAllBookings_whenDeleteAllBookings_givenBookingsExists.sql")
    public void shouldDeleteABooking_whenDeleteBookingById_givenBookingExists() {
        List<TourBooking> bookingsBefore = tourBookingService.getAllBookings();
        assertThat(bookingsBefore.size()).isEqualTo(3);

        tourBookingService.deleteBookingById(BOOKING_ID);

        List<TourBooking> bookingsAfter = tourBookingService.getAllBookings();
        assertThat(bookingsAfter.size()).isEqualTo(2);
    }

    @Test
    @Sql("classpath:com/bookinggo/RESTfulDemo/service/TourBookingServiceIT.shouldDeleteAllBookings_whenDeleteAllBookings_givenBookingsExists.sql")
    public void shouldThrowTourBookingServiceException_whenDeleteBookingById_givenBookingDoesntExist() {
        assertThrows(TourBookingServiceException.class,
                () -> tourBookingService.deleteBookingById(NON_EXISTING_BOOKING_ID));
    }

    private static Stream<Arguments> dateTimeAndLocationAndParticipantsAndBookingProvider() {
        return Stream.of(
                Arguments.of(PICKUP_DATE_TIME, null, null,
                        buildBooking(PICKUP_DATE_TIME, ORIGINAL_PICKUP_LOCATION, ORIGINAL_PARTICIPANTS)),
                Arguments.of(null, PICKUP_LOCATION, null,
                        buildBooking(ORIGINAL_PICKUP_DATE_TIME, PICKUP_LOCATION, ORIGINAL_PARTICIPANTS)),
                Arguments.of(null, null, PARTICIPANTS,
                        buildBooking(ORIGINAL_PICKUP_DATE_TIME, ORIGINAL_PICKUP_LOCATION, PARTICIPANTS)),
                Arguments.of(PICKUP_DATE_TIME, PICKUP_LOCATION, null,
                        buildBooking(PICKUP_DATE_TIME, PICKUP_LOCATION, ORIGINAL_PARTICIPANTS)),
                Arguments.of(PICKUP_DATE_TIME, null, PARTICIPANTS,
                        buildBooking(PICKUP_DATE_TIME, ORIGINAL_PICKUP_LOCATION, PARTICIPANTS)),
                Arguments.of(null, PICKUP_LOCATION, PARTICIPANTS,
                        buildBooking(ORIGINAL_PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS)),
                Arguments.of(PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS,
                        buildBooking(PICKUP_DATE_TIME, PICKUP_LOCATION, PARTICIPANTS)));
    }

    static private TourBooking buildBooking(LocalDateTime dateTime, String location, int participants) {
        return TourBooking.builder()
                .pickupDateTime(dateTime)
                .pickupLocation(location)
                .participants(participants)
                .build();
    }
}

