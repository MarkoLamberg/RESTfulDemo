package com.bookinggo.RESTfulDemo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@Table(name="tour_rating")
public class TourRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column(name="customer_id")
    private Integer customerId;

    @Column(nullable = false)
    private Integer score;

    @Column
    private String comment;

    public TourRating(Tour tour, Integer customerId, Integer score, String comment) {
        this.tour = tour;
        this.customerId = customerId;
        this.score = score;
        this.comment = comment;
    }

    public TourRating(Tour tour, Integer customerId, Integer score) {
        this.tour = tour;
        this.customerId = customerId;
        this.score = score;
        this.comment = toComment(score);
    }

    private String toComment(Integer score) {
        switch (score) {
            case 1:return "Terrible";
            case 2:return "Poor";
            case 3:return "Fair";
            case 4:return "Good";
            case 5:return "Great";
            default: return score.toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        TourRating that = (TourRating) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(tour, that.tour) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(score, that.score) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tour, customerId, score, comment);
    }
}
