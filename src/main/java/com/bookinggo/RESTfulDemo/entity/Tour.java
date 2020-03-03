package com.bookinggo.RESTfulDemo.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tour implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String title;

    @Column
    private Integer price;

    @Column
    private String length;

    @Column
    private String duration;

    @Column
    private String packageType;

    @ManyToOne
    private TourPackage tourPackage;

    @Column
    private Region region;

    public Tour(String title, Integer price, String duration, TourPackage tourPackage, Region region){
        this.title = title;
        this.price = price;
        this.duration = duration;
        this.tourPackage = tourPackage;
        this.region = region;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", duration='" + duration + '\'' +
                ", tourPackage=" + tourPackage +
                ", region=" + region +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tour tour = (Tour) o;

        return Objects.equals(id, tour.id) &&
                Objects.equals(title, tour.title) &&
                Objects.equals(price, tour.price) &&
                Objects.equals(duration, tour.duration) &&
                Objects.equals(tourPackage, tour.tourPackage) &&
                region == tour.region;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, duration, tourPackage, region);
    }
}