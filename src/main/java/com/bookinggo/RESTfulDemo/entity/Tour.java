package com.bookinggo.RESTfulDemo.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Slf4j
@EqualsAndHashCode
public class Tour implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private Integer price;

    @ManyToOne
    @JoinColumn(name = "tour_package_code")
    private TourPackage tourPackage;

    public Tour(String title, Integer price, String duration,
                com.bookinggo.RESTfulDemo.entity.TourPackage tourPackage) {
        log.info("constructor - title: {}, price: {}, duration: {}", title, price, duration);

        this.title = title;
        this.price = price;
        this.tourPackage = tourPackage;
    }
}
