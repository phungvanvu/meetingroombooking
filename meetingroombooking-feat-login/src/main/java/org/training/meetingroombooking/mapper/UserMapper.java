package org.training.meetingroombooking.mapper;

import org.training.meetingroombooking.dto.UserDTO;
import org.training.meetingroombooking.model.User;

public interface UserMapper {
    User toUser(UserDTO userDTO);
    void updateUser(User user, UserDTO userDTO);
}
