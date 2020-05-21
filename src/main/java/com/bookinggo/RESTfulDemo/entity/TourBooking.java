package com.bookinggo.RESTfulDemo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Slf4j
@Table(name = "tour_booking")
@EqualsAndHashCode
public class TourBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "pickup_date")
    private String date;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(name = "num_of_participants")
    private Integer participants;

    public TourBooking(Tour tour, Customer customer, String date, String pickupLocation, Integer participants) {
        log.info("constructor - tour: {}, customerId: {}, date: {}, pickupLocation: {}, participants: {}", tour, date, pickupLocation, participants);

        this.tour = tour;
        this.customer = customer;
        this.date = date;
        this.pickupLocation = pickupLocation;
        this.participants = participants;
    }

    public String getTotalPriceString() {
        int total = getParticipants() * getTour().getPrice();

        return "£" + total + ".00";
    }
}
