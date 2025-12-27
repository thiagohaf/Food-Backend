package com.thiagoferreira.food_backend.mappers;

import com.thiagoferreira.food_backend.domain.dto.UserRequest;
import com.thiagoferreira.food_backend.domain.dto.UserResponse;
import com.thiagoferreira.food_backend.domain.dto.UserUpdateRequest;
import com.thiagoferreira.food_backend.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserRequest dto);

    UserResponse toResponse(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "login", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    void updateEntityFromDto(UserUpdateRequest dto, @MappingTarget User entity);


    // preciso mudar onde utiliza a model para utilizar a DTO e fazer os mappers correspondentes, preciso verificar se os tratamentos de erro estao corretos
}
