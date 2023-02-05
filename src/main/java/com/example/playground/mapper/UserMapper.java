package com.example.playground.mapper;

import com.example.playground.quote.api.response.UserResponse;
import com.example.playground.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserResponse mapToUserResponse(User user);
}
