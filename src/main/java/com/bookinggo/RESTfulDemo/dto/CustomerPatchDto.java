package com.bookinggo.RESTfulDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Override
    public String toString() {
        return String.format("CustomerPatchDto: %s, %s", title, name);
    }
}
