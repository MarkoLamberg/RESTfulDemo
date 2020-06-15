package com.bookinggo.RESTfulDemo.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tour_package")
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class TourPackage implements Serializable {

    @Id
    private String code;

    private String name;

    private String location;
}
