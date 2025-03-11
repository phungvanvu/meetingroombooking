package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.dto.Request.UserRequest;
import org.training.meetingroombooking.dto.Response.UserResponse;
import org.training.meetingroombooking.entity.models.Position.User;

public interface UserMapper {
    User toEntity(UserRequest request);
    UserResponse toDTO(User user);
    void updateEntity(User user, UserRequest request);
}
