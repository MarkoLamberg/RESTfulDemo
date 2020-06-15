package com.bookinggo.RESTfulDemo.dto;

import lombok.*;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TourDto {

    @Size(max = 2)
    @NotNull
    private String tourPackageCode;

    @Size(max = 100)
    @NotNull
    private String title;

    @Size(max = 32)
    @NotNull
    private String duration;

    @Min(1)
    private Integer price;

}