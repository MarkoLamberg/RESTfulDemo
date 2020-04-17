package com.bookinggo.RESTfulDemo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Table(name = "tour_package")
@Entity
@Data
@NoArgsConstructor
@Slf4j
public class TourPackage implements Serializable {

    @Id
    private String code;

    @Column
    private String name;

    @Column
    private Location location;

    public TourPackage(String code, String name, Location location) {
        log.info("constructor - code: {}, name: {}, location: {}", code, name, location);

        this.code = code;
        this.name = name;
        this.location = location;
    }

    @Override
    public String toString() {
        return "TourPackage{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", region=" + location +
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

        TourPackage that = (TourPackage) o;

        return Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, location);
    }
}
