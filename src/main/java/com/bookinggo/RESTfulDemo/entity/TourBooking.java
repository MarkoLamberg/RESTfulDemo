package com.bookinggo.RESTfulDemo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "pickup_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime pickupDateTime;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(name = "num_of_participants")
    private Integer participants;

    @CreationTimestamp
    @ToString.Exclude
    @Column(name = "created_when", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdWhen;

    @ToString.Exclude
    @Column(name = "modified_when", columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedWhen;

    @PreUpdate
    public void onUpdate() {
        modifiedWhen = LocalDateTime.now();
    }

    public TourBooking(Tour tour, Customer customer, LocalDateTime pickupDateTime, String pickupLocation, Integer participants) {
        log.info("constructor - tour: {}, customerId: {}, date: {}, pickupLocation: {}, participants: {}", tour, pickupDateTime, pickupLocation, participants);

        this.tour = tour;
        this.customer = customer;
        this.pickupDateTime = pickupDateTime;
        this.pickupLocation = pickupLocation;
        this.participants = participants;
    }

    public String getTotalPriceString() {
        int total = getParticipants() * getTour().getPrice();

        return "£" + total + ".00";
    }
}
