package com.bookinggo.RestfulDemo.dto;

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
public class BookingPatchDto {

    @Size(max = 255)
    private String pickupDateTime;

    @Size(max = 255)
    private String pickupLocation;

    @NotNull
    @Positive
    private Integer customerId;

    @Positive
    private Integer participants;

    private String totalPrice;

    @Override
    public String toString() {
        return String.format("BookingPatchDto: %s, %s, %d, %d, %s",
                pickupDateTime, pickupLocation, customerId, participants, totalPrice);
    }
}
