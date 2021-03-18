package com.bookinggo.RestfulDemo.entity;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(example = "AA")
    private String code;

    @ApiModelProperty(example = "London Sightseeing")
    private String name;

    @ApiModelProperty(example = "London")
    private String location;
}
