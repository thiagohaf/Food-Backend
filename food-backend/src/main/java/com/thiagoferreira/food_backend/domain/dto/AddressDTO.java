package com.thiagoferreira.food_backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDTO {

    @NotBlank(message = "Street is required.")
    private String street;

    @NotBlank(message = "Number is required.")
    private String number;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "ZipCode is required.")
    private String zipCode;
}
