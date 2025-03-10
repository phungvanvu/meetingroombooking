package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.entity.models.User;

public interface UserMapper {
    User toEntity(UserRequest request);
    UserResponse toDTO(User user);
    void updateEntity(User user, UserRequest request);
}
