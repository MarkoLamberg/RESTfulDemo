package com.bookinggo.RestfulDemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class ExpandedBookingDto extends BookingDto {

    @NotNull
    @Positive
    @ApiModelProperty(example = "1")
    private Integer bookingId;

    @NotNull
    @Positive
    @ApiModelProperty(example = "1")
    private Integer tourId;
}
