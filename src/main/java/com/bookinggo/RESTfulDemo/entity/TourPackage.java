package com.bookinggo.RESTfulDemo.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tour_package")
@Entity
@Data
@NoArgsConstructor
@Slf4j
@EqualsAndHashCode
public class TourPackage implements Serializable {

    @Id
    private String code;

    private String name;

    private String location;

    public TourPackage(String code, String name, String location) {
        log.info("constructor - code: {}, name: {}, location: {}", code, name, location);

        this.code = code;
        this.name = name;
        this.location = location;
    }
}
