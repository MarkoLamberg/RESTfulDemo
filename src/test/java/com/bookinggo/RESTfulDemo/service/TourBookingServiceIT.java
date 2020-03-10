/*
package com.example.ec.service;

import com.bookinggo.RESTfulDemo.entity.TourBooking;
import com.bookinggo.RESTfulDemo.service.TourBookingService;
import com.example.ec.domain.TourBooking;
import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TourBookingServiceIT {
    private static final int CUSTOMER_ID = 456;
    private static final int TOUR_ID = 1;
    private static final int NOT_A_TOUR_ID = 123;

    @Autowired
    private TourBookingService service;

    //Happy Path delete existing TourBooking.
    @Test
    public void delete() {
        List<TourBooking> tourBookings = service.lookupAll();
        service.delete(tourBookings.get(0).getTour().getId(), tourBookings.get(0).getCustomerId());
        assertThat(service.lookupAll().size(), is(tourBookings.size() - 1));
    }

    //UnHappy Path, Tour NOT_A_TOUR_ID does not exist
    @Test(expected = NoSuchElementException.class)
    public void deleteException() {
        service.delete(NOT_A_TOUR_ID, 1234);
    }


    //Happy Path to Create a new Tour Booking
    @Test
    public void createNew() {
        //would throw NoSuchElementException if TourBooking for TOUR_ID by CUSTOMER_ID already exists
        service.createNew(TOUR_ID, CUSTOMER_ID, 2, "it was fair");

        //Verify New Tour Booking created.
        TourBooking newTourBooking = service.verifyTourBooking(TOUR_ID, CUSTOMER_ID);
        assertThat(newTourBooking.getTour().getId(), is(TOUR_ID));
        assertThat(newTourBooking.getCustomerId(), is(CUSTOMER_ID));
        assertThat(newTourBooking.getScore(), is(2));
        assertThat(newTourBooking.getComment(), is ("it was fair"));
    }

    //UnHappy Path, Tour NOT_A_TOUR_ID does not exist
    @Test(expected = NoSuchElementException.class)
    public void createNewException() {
        service.createNew(NOT_A_TOUR_ID, CUSTOMER_ID, 2, "it was fair");
    }

    //Happy Path many customers Rate one tour
    @Test
    public void rateMany() {
        int Bookings = service.lookupAll().size();
        service.rateMany(TOUR_ID, 5, new Integer[]{100, 101, 102});
        assertThat(service.lookupAll().size(), is(Bookings + 3));
    }

    //Unhappy Path, 2nd Invocation would create duplicates in the database, DataIntegrityViolationException thrown
    @Test(expected = DataIntegrityViolationException.class)
    public void rateManyProveRollback() {
        int Bookings = service.lookupAll().size();
        Integer customers[] = {100, 101, 102};
        service.rateMany(TOUR_ID, 3, customers);
        service.rateMany(TOUR_ID, 3, customers);
    }

    //Happy Path, Update a Tour Booking already in the database
    @Test
    public void update() {
        createNew();
        TourBooking tourBooking = service.update(TOUR_ID, CUSTOMER_ID, 1, "one");
        assertThat(tourBooking.getTour().getId(), is(TOUR_ID));
        assertThat(tourBooking.getCustomerId(), is(CUSTOMER_ID));
        assertThat(tourBooking.getScore(), is(1));
        assertThat(tourBooking.getComment(), is("one"));
    }

    //Unhappy path, no Tour Booking exists for tourId=1 and customer=1
    @Test(expected = NoSuchElementException.class)
    public void updateException() throws Exception {
        service.update(1, 1, 1, "one");
    }

    //Happy Path, Update a Tour Booking already in the database
    @Test
    public void updateSome() {
        createNew();
        TourBooking tourBooking = service.update(TOUR_ID, CUSTOMER_ID, 1, "one");
        assertThat(tourBooking.getTour().getId(), is(TOUR_ID));
        assertThat(tourBooking.getCustomerId(), is(CUSTOMER_ID));
        assertThat(tourBooking.getScore(), is(1));
        assertThat(tourBooking.getComment(), is("one"));
    }

    //Unhappy path, no Tour Booking exists for tourId=1 and customer=1
    @Test(expected = NoSuchElementException.class)
    public void updateSomeException() throws Exception {
        service.update(1, 1, 1, "one");
    }

    //UnHappy Path, Tour NOT_A_TOUR_ID does not exist
    @Test(expected = NoSuchElementException.class)
    public void getAverageScoreException() {
        service.getAverageScore(NOT_A_TOUR_ID); //That tour does not exist
    }
}
*/
