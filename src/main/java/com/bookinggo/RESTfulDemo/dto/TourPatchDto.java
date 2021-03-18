package com.bookinggo.RestfulDemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TourPatchDto {

    @Size(max = 2)
    @ApiModelProperty(example = "AA")
    private String tourPackageCode;

    @Size(max = 100)
    @ApiModelProperty(example = "Wembley Stadium Tour")
    private String title;

    @Size(max = 32)
    @ApiModelProperty(example = "2 hours")
    private String duration;

    @Min(1)
    @ApiModelProperty(example = "20")
    private Integer price;

    @Override
    public String toString() {
        return String.format("TourPatchDto: %s, %s, %s, %d",
                tourPackageCode, title, duration, price);
    }
}