package com.bookinggo.RESTfulDemo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * The Tour contains all attributes of an Explore California Tour.
 *
 * Created by Mary Ellen Bowman
 */
@Entity
@Data
@NoArgsConstructor
public class Tour implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private Integer price;

    @ManyToOne
    @JoinColumn(name="tour_package_code")
    private TourPackage tourPackage;

    @Column
    private Region region;

    public Tour(String title, Integer price, String duration,
                com.bookinggo.RESTfulDemo.entity.TourPackage tourPackage, Region region) {
        this.title = title;
        this.price = price;
        this.tourPackage = tourPackage;
        this.region = region;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", tourPackage=" + tourPackage +
                ", region=" + region +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tour tour = (Tour) o;
        return Objects.equals(id, tour.id) &&
                Objects.equals(title, tour.title) &&
                Objects.equals(price, tour.price) &&
                Objects.equals(tourPackage, tour.tourPackage) &&
                region == tour.region;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, tourPackage, region);
    }
}
