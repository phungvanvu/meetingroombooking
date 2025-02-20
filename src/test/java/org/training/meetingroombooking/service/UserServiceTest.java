package org.training.meetingroombooking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.training.meetingroombooking.dto.UserDTO;
import org.training.meetingroombooking.entity.Role;
import org.training.meetingroombooking.entity.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.mapper.UserMapper;
import org.training.meetingroombooking.repository.RoleRepository;
import org.training.meetingroombooking.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO.Builder()
                .userName("ptlinh")
                .fullName("John Doe")
                .email("john.doe@example.com")
                .password("P@ssw0rd")
                .roles(Set.of("HR"))
                .build();

        user = new User();
        user.setUserName("ptlinh");
        user.setFullName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("hashed_password");
    }

    //unit test từng phương thức
    @Test

    void createUser_Success() {
        // Giả lập user chưa tồn tại
        when(userRepository.existsByUserName(userDTO.getUserName())).thenReturn(false);

        // Chuyển DTO thành entity
        when(userMapper.toEntity(userDTO)).thenReturn(user);

        // Mã hóa mật khẩu
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");

        // Giả lập lấy danh sách role
        Role role = new Role("USER", "User role description", new HashSet<>());
        when(roleRepository.findByRoleNameIn(anySet())).thenReturn(Set.of(role));

        // Giả lập lưu user vào database
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Gọi hàm service
        UserDTO result = userService.createUser(userDTO);

        // Kiểm tra kết quả
        assertNotNull(result);
        assertEquals(userDTO.getUserName(), result.getUserName());
        verify(userRepository).save(any(User.class));
    }



    @Test
    void createUser_UserAlreadyExists_ThrowsException() {
        when(userRepository.existsByUserName(userDTO.getUserName())).thenReturn(true);

        assertThrows(AppEx.class, () -> userService.createUser(userDTO));
    }

    @Test
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }


    @Test
    void getUserById_UserExists_ReturnsUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(userDTO.getUserName(), result.getUserName());
    }

    @Test
    void getUserById_UserNotFound_ThrowsException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(AppEx.class, () -> userService.getUserById(1));
    }

    @Test
    void updateUser_Success() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        UserDTO updatedUser = new UserDTO.Builder()
                .userName("updated_john")
                .fullName("Updated John Doe")
                .email("updated@example.com")
                .build();

        UserDTO result = userService.updateUser(1, updatedUser);

        assertNotNull(result);
        assertEquals("updated_john", result.getUserName());
    }

    @Test
    void updateUser_UserNotFound_ThrowsException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(AppEx.class, () -> userService.updateUser(1, userDTO));
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyInt());

        assertDoesNotThrow(() -> userService.deleteUser(1));

        verify(userRepository).deleteById(1);
    }

    @Test
    void deleteUser_UserNotFound_ThrowsException() {
        when(userRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(AppEx.class, () -> userService.deleteUser(1));
    }


}
