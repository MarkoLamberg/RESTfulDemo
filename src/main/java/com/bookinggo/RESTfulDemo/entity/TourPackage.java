package com.bookinggo.RESTfulDemo.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "tour_package")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TourPackage extends AbstractEntity {

    @Id
    private String code;

    private String name;

    private String location;
}
