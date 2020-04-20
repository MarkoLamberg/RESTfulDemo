package com.bookinggo.RESTfulDemo.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Slf4j
@Table(name = "tour_booking")
@EqualsAndHashCode
public class TourBooking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "pickup_date")
    private String date;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(name = "num_of_participants")
    private Integer participants;

    public TourBooking(Tour tour, Integer customerId, String date, String pickupLocation, Integer participants) {
        log.info("constructor - tour: {}, customerId: {}, date: {}, pickupLocation: {}, participants: {}", tour, date, pickupLocation, participants);

        this.tour = tour;
        this.customerId = customerId;
        this.date = date;
        this.pickupLocation = pickupLocation;
        this.participants = participants;
    }

    public String getTotalPriceString() {
        int total = getParticipants() * getTour().getPrice();

        return "£" + total + ".00";
    }
}
