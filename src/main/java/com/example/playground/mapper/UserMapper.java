package com.example.playground.mapper;

import com.example.playground.quote.api.response.UserResponse;
import com.example.playground.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserResponse mapToUserResponse(User user);
}
