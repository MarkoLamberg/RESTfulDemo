package com.bookinggo.RESTfulDemo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ExpandedBookingDto extends BookingDto{
    @NotNull
    @Positive
    private Integer tourId;

    public ExpandedBookingDto(@Size(max = 255) String date, @Size(max = 255) String pickupLocation, @NotNull Integer customerId,
                              @NotNull @Min(1) Integer partisipants, String totalPrice, @NotNull Integer tourId) {
        super(date, pickupLocation, customerId, partisipants, totalPrice);
        this.tourId = tourId;
    }
}
