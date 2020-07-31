package com.bookinggo.RESTfulDemo.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TourPatchDto {

    @Size(max = 2)
    private String tourPackageCode;

    @Size(max = 100)
    private String title;

    @Size(max = 32)
    private String duration;

    @Min(1)
    private Integer price;

}