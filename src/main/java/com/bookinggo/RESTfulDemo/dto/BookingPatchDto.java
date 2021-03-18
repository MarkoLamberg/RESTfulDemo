package com.bookinggo.RestfulDemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingPatchDto {

    @Size(max = 255)
    @ApiModelProperty(example = "2020-05-26T17:30:00")
    private String pickupDateTime;

    @Size(max = 255)
    @ApiModelProperty(example = "Hotel Plaza")
    private String pickupLocation;

    @NotNull
    @Positive
    @ApiModelProperty(example = "1")
    private Integer customerId;

    @Positive
    @ApiModelProperty(example = "2")
    private Integer participants;

    @ApiModelProperty(example = "£20.00")
    private String totalPrice;

    @Override
    public String toString() {
        return String.format("BookingPatchDto: %s, %s, %d, %d, %s",
                pickupDateTime, pickupLocation, customerId, participants, totalPrice);
    }
}
