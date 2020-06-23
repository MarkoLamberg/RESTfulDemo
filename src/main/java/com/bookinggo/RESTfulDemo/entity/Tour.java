package com.bookinggo.RESTfulDemo.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Tour implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private Integer price;

    private String duration;

    @ManyToOne
    @JoinColumn(name = "tour_package_code")
    private TourPackage tourPackage;

    @CreationTimestamp
    @ToString.Exclude
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdWhen;
}
