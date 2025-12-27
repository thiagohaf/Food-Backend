package com.thiagoferreira.food_backend.domain.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {
    private String street;
    private String number;
    private String city;
    private String zipCode;
}
