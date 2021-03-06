package com.bookinggo.RestfulDemo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tour_booking")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TourBooking extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "1")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "pickup_date_time", columnDefinition = "TIMESTAMP")
    @ApiModelProperty(example = "2020-05-26T17:30:00")
    private LocalDateTime pickupDateTime;

    @Column(nullable = false)
    @ApiModelProperty(example = "Hotel Plaza")
    private String pickupLocation;

    @Column
    @ApiModelProperty(example = "2")
    private Integer participants;

    @ApiModelProperty(example = "£20.00")
    public String getTotalPriceString() {
        int total = participants * tour.getPrice();

        return "£" + total + ".00";
    }
}
