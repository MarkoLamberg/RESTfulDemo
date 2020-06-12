package com.bookinggo.RESTfulDemo.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDto {

    @Size(max = 5)
    @NotNull
    private String title;

    @Size(max = 50)
    @NotNull
    private String name;
}
