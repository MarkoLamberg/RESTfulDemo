package com.bookinggo.RESTfulDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingDto {

    @Size(max = 255)
    @NotNull
    private String pickupDateTime;

    @Size(max = 255)
    @NotNull
    private String pickupLocation;

    @NotNull
    @Positive
    private Integer customerId;

    @NotNull
    @Positive
    private Integer participants;

    private String totalPrice;

    @Override
    public String toString() {
        return String.format("BookingDto: %s, %s, %d, %d, %s",
                pickupDateTime, pickupLocation, customerId, participants, totalPrice);
    }
}
