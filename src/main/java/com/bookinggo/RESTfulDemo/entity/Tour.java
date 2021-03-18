package com.bookinggo.RestfulDemo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tour extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "1")
    private Integer id;

    @ApiModelProperty(example = "Wembley Stadium Tour")
    private String title;

    @ApiModelProperty(example = "20")
    private Integer price;

    @ApiModelProperty(example = "2.5 hours")
    private String duration;

    @ManyToOne
    @JoinColumn(name = "tour_package_code")
    private TourPackage tourPackage;
}
