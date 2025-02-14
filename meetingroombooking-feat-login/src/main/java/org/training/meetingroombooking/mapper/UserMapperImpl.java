//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.training.meetingroombooking.mapper;

import org.springframework.stereotype.Component;
import org.training.meetingroombooking.dto.UserDTO;
import org.training.meetingroombooking.model.User;

@Component
public class UserMapperImpl implements UserMapper {
    public UserMapperImpl() {
    }

    public User toUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            user.setUserName(userDTO.getUserName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            return user;
        }
    }

    public void updateUser(User user, UserDTO userDTO) {
        if (userDTO != null && user != null) {
            user.setUserName(userDTO.getUserName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
        }
    }
}
