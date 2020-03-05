package com.bookinggo.RESTfulDemo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Table(name="tour_package")
@Entity
@Data
@NoArgsConstructor
public class TourPackage implements Serializable {
    @Id
    private String code;

    @Column
    private String name;

    @Column
    private Location location;

    public TourPackage(String code, String name, Location location) {
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
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
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
