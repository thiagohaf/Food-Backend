package com.thiagoferreira.food_backend.domain.entities;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
@Hidden
public class Address {
    private String street;
    private String number;
    private String city;
    private String zipCode;
}
