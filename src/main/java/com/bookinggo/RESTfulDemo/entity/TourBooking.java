package com.bookinggo.RESTfulDemo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@Table(name="tour_booking")
public class TourBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column(name="customer_id")
    private Integer customerId;

    @Column(name="pickup_date")
    private String date;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(name="num_of_partisipants")
    private Integer partisipants;

    public TourBooking(Tour tour, Integer customerId, String date, String pickupLocation, Integer partisipants) {
        this.tour = tour;
        this.customerId = customerId;
        this.date = date;
        this.pickupLocation = pickupLocation;
        this.partisipants = partisipants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        TourBooking that = (TourBooking) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(tour, that.tour) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(date, that.date) &&
                Objects.equals(pickupLocation, that.pickupLocation) &&
                Objects.equals(partisipants, that.partisipants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tour, customerId, date, pickupLocation, partisipants);
    }

    public String getTotalPriceString(){
        int total = getPartisipants() * getTour().getPrice();

        return "£" + total + ".00";
    }
}
