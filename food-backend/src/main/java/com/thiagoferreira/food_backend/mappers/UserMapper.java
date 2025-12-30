package com.thiagoferreira.food_backend.mappers;

import com.thiagoferreira.food_backend.domain.dto.AddressDTO;
import com.thiagoferreira.food_backend.domain.dto.UserRequest;
import com.thiagoferreira.food_backend.domain.dto.UserResponse;
import com.thiagoferreira.food_backend.domain.dto.UserUpdateRequest;
import com.thiagoferreira.food_backend.domain.entities.Address;
import com.thiagoferreira.food_backend.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        user.setType(dto.getType());
        user.setAddress(toAddressEntity(dto.getAddress()));

        return user;
    }

    public UserResponse toResponse(User entity) {
        if (entity == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setEmail(entity.getEmail());
        response.setLogin(entity.getLogin());
        response.setType(entity.getType());
        response.setAddress(toAddressDTO(entity.getAddress()));
        response.setLastUpdate(entity.getLastUpdated());

        return response;
    }

    public void updateEntityFromDto(UserUpdateRequest dto, User entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setAddress(toAddressEntity(dto.getAddress()));
    }

    private Address toAddressEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }

        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setNumber(dto.getNumber());
        address.setCity(dto.getCity());
        address.setZipCode(dto.getZipCode());

        return address;
    }

    private AddressDTO toAddressDTO(Address entity) {
        if (entity == null) {
            return null;
        }

        AddressDTO dto = new AddressDTO();
        dto.setStreet(entity.getStreet());
        dto.setNumber(entity.getNumber());
        dto.setCity(entity.getCity());
        dto.setZipCode(entity.getZipCode());

        return dto;
    }
}
