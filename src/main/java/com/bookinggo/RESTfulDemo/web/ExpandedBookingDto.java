package com.bookinggo.RESTfulDemo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpandedBookingDto {

    @Size(max = 255)
    private String date;

    @Size(max = 255)
    private String pickupLocation;

    @NotNull
    private Integer tourId;

    @NotNull
    private Integer customerId;

    @NotNull
    @Min(1)
    private Integer partisipants;

    private String totalPrice;
}
