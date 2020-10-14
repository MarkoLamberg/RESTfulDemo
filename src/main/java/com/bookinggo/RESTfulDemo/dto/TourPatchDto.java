package com.bookinggo.RestfulDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Override
    public String toString() {
        return String.format("TourPatchDto: %s, %s, %s, %d",
                tourPackageCode, title, duration, price);
    }
}