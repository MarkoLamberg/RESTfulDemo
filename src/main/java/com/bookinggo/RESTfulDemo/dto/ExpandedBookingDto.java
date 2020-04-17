package com.bookinggo.RESTfulDemo.dto;

import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
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
