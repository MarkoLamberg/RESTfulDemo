package com.bookinggo.RESTfulDemo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tour_booking")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TourBooking extends AbstractEntity {

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

    @Column
    private Integer participants;

    public String getTotalPriceString() {
        int total = getParticipants() * getTour().getPrice();

        return "£" + total + ".00";
    }
}
