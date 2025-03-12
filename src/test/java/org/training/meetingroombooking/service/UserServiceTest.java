package org.training.meetingroombooking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
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

  @Mock
  private Authentication authentication;  // Định nghĩa Authentication

  @Mock
  private SecurityContext securityContext;

  @InjectMocks
  private UserService userService;

  private User user;
  private UserRequest request;
  private UserResponse response;

  @BeforeEach
  void setUp() {
    request = new UserRequest().Builder()
        .userName("ptlinh")
        .fullName("Phung Thi Linh")
        .email("john.doe@example.com")
        .password("P@ssw0rd")
        .roles(Set.of("HR"))
        .build();

    user = new User();
    user.setUserName("ptlinh");
    user.setFullName("Phung Thi Linh");
    user.setEmail("john.doe@example.com");
    user.setPassword("hashed_password");
  }

  //unit ***REMOVED*** từng phương thức
  @Test

  void createUser_Success() {
    // Giả lập user chưa tồn tại
    when(userRepository.existsByUserName(userDTO.getUserName())).thenReturn(false);

    // Chuyển DTO thành entity
    when(userMapper.toEntity(userDTO)).thenReturn(user);


    // Giả lập lấy danh sách role
    Role role = new Role("USER", "User role description", new HashSet<>());
    when(roleRepository.findByRoleNameIn(anySet())).thenReturn(Set.of(role));

    // Giả lập lưu user vào database
    when(userRepository.save(any(User.class))).thenReturn(user);

    // **Bổ sung mock cho userMapper.toDTO()**
    when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

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
        .userName("ptlinh")
        .fullName("Updated John Doe")
        .email("updated@example.com")
        .build();

    UserDTO result = userService.updateUser(1, updatedUser);

    assertNotNull(result);
    assertEquals("ptlinh", result.getUserName());
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

  @Test
  void getMyInfo_Success() {
    // Giả lập user đăng nhập
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("***REMOVED***User");
    SecurityContextHolder.setContext(securityContext);

    // Giả lập tìm thấy user trong DB
    User user = new User();
    user.setUserName("***REMOVED***User");
    when(userRepository.findByUserName("***REMOVED***User")).thenReturn(Optional.of(user));

    // Giả lập ánh xạ sang DTO
    UserDTO userDTO = new UserDTO();
    userDTO.setUserName("***REMOVED***User");
    when(userMapper.toDTO(user)).thenReturn(userDTO);

    // Gọi phương thức service
    UserDTO result = userService.getMyInfo();

    // Kiểm tra kết quả
    assertNotNull(result);
    assertEquals("***REMOVED***User", result.getUserName());
    verify(userRepository).findByUserName("***REMOVED***User");
    verify(userMapper).toDTO(user);
  }

  @Test
  void getMyInfo_UserNotFound() {
    // Giả lập user đăng nhập
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("unknownUser");
    SecurityContextHolder.setContext(securityContext);

    // Giả lập không tìm thấy user trong DB
    when(userRepository.findByUserName("unknownUser")).thenReturn(Optional.empty());

    // Kiểm tra ngoại lệ
    AppEx exception = assertThrows(AppEx.class, () -> userService.getMyInfo());

    // Kiểm tra mã lỗi
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    verify(userRepository).findByUserName("unknownUser");
  }

}
