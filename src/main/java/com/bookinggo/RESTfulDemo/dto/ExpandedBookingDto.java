package com.bookinggo.RESTfulDemo.dto;

import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class ExpandedBookingDto extends BookingDto {

    @NotNull
    @Positive
    private Integer tourId;

    @Builder(builderMethodName = "childBuilder")
    public ExpandedBookingDto(@Size(max = 255) String pickupDateTime, @Size(max = 255) String pickupLocation, @NotNull Integer customerId,
                              @NotNull @Min(1) Integer participants, String totalPrice, @NotNull Integer tourId) {
        super(pickupDateTime, pickupLocation, customerId, participants, totalPrice);
        this.tourId = tourId;
    }
}
