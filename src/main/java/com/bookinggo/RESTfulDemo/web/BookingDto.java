package com.bookinggo.RESTfulDemo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    @Size(max = 255)
    @NotNull
    private String date;

    @Size(max = 255)
    @NotNull
    private String pickupLocation;

    @NotNull
    @Positive
    private Integer customerId;

    @NotNull
    @Positive
    private Integer partisipants;

    @NotNull
    @Positive
    private String totalPrice;
}
