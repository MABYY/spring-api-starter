package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.UserDTO;
import com.codewithmosh.store.dtos.UserRegisterDTO;
import com.codewithmosh.store.dtos.UserUpdateDTO;
import com.codewithmosh.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UMapper {
    UserDTO toUserDto(User user);
    User toEntity( UserRegisterDTO request);
    void updateUser(UserUpdateDTO request, @MappingTarget User user );
}
