package com.bookinggo.RestfulDemo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class ExpandedBookingDto extends BookingDto {

    @NotNull
    @Positive
    private Integer tourId;
}
