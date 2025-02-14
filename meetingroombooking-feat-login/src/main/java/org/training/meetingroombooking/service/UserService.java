package org.training.meetingroombooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.dto.UserDTO;
import org.training.meetingroombooking.mapper.UserMapper;
import org.training.meetingroombooking.model.User;
import org.training.meetingroombooking.repository.UserRepository;

import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper UserMapper;

    public User CreateUser(UserDTO userDTO) {
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new RuntimeException("User name already exists");
        }
        User user = UserMapper.toUser(userDTO);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    public User updateUser(int UserId, UserDTO userDTO) {
        User user = getUserById(UserId);
        UserMapper.updateUser(user, userDTO);

        return userRepository.save(user);
    }

    public void deleteUser(int UserId) {
        userRepository.deleteById(UserId);
    }

}
