package com.bookinggo.RESTfulDemo.dto;

import lombok.*;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerPatchDto {

    @Size(max = 5)
    private String title;

    @Size(max = 50)
    private String name;
}
