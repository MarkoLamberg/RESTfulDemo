package com.bookinggo.RESTfulDemo.dto;

import lombok.*;

import javax.validation.constraints.*;

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
}
