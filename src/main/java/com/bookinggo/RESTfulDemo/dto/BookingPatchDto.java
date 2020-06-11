package com.bookinggo.RESTfulDemo.dto;

import lombok.*;

import javax.validation.constraints.*;

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
}
