package com.bookinggo.RestfulDemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TourDto {

    @Size(max = 2)
    @NotNull
    @ApiModelProperty(example = "AA")
    private String tourPackageCode;

    @Size(max = 100)
    @NotNull
    @ApiModelProperty(example = "Wembley Stadium Tour")
    private String title;

    @Size(max = 32)
    @NotNull
    @ApiModelProperty(example = "2 hours")
    private String duration;

    @Min(1)
    @ApiModelProperty(example = "20")
    private Integer price;

    @Override
    public String toString() {
        return String.format("TourDto: %s, %s, %s, %d",
                tourPackageCode, title, duration, price);
    }

}