package org.training.meetingroombooking.mapper;

import org.training.meetingroombooking.dto.UserDTO;
import org.training.meetingroombooking.entity.User;

public interface UserMapper {
    User toEntity(UserDTO userDTO);
    UserDTO toDTO(User user);
    void updateEntity(User user, UserDTO userDTO);
}
