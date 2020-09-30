package com.bookinggo.RESTfulDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @Override
    public String toString() {
        return String.format("TourDto: %s, %s, %s, %d",
                tourPackageCode, title, duration, price);
    }

}