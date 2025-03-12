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
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.UserMapper;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.entity.models.Position;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoleRepository;
import org.training.meetingroombooking.repository.UserRepository;
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
  private Authentication authentication;

  @Mock
  private SecurityContext securityContext;

  @InjectMocks
  private UserService userService;

  private User user;
  private UserRequest request;
  private UserResponse response;

  @BeforeEach
  void setUp() {
    Position position = new Position();
    position.setPositionName("Manager");

    GroupEntity group = new GroupEntity();
    group.setGroupName("Group 1");

    request = new UserRequest();
    request.setUserName("ptlinh");
    request.setFullName("Phung Thi Linh");
    request.setDepartment("Human Resources");
    request.setEmail("john.doe@example.com");
    request.setPhoneNumber("0123456789");
    request.setPassword("P@ssw0rd");
    request.setEnabled(true);
    request.setPosition(position);
    request.setGroup(group);
    request.setRoles(Set.of("HR"));

    user = new User();
    user.setUserName("ptlinh");
    user.setFullName("Phung Thi Linh");
    user.setDepartment("Human Resources");
    user.setEmail("john.doe@example.com");
    user.setPhoneNumber("0123456789");
    user.setPassword("hashed_password");
    user.setEnabled(true);
    user.setPosition(position);
    user.setGroup(group);
    user.setRoles(Set.of(Role.builder().roleName("HR").build()));

    response = new UserResponse();
    response.setUserId(1L);
    response.setUserName("ptlinh");
    response.setFullName("Phung Thi Linh");
    response.setDepartment("Human Resources");
    response.setEmail("john.doe@example.com");
    response.setPhoneNumber("0123456789");
    response.setPositionName("Manager");
    response.setGroupName("HR Department");
    response.setRoles(Set.of("HR"));
    response.setEnabled(true);
  }

  @Test
  void createUser_Success() {
    when(userRepository.existsByUserName(request.getUserName())).thenReturn(false);
    when(roleRepository.findByRoleNameIn(anySet()))
            .thenReturn(Set.of(Role.builder().roleName("HR").build()));
    when(userMapper.toEntity(any(UserRequest.class))).thenReturn(user);
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(userMapper.toUserResponse(any(User.class))).thenReturn(response);

    UserResponse result = userService.createUser(request);

    assertNotNull(result);
    assertEquals(response.getUserName(), result.getUserName());
    verify(userRepository).existsByUserName(request.getUserName());
    verify(userRepository).save(any(User.class));
    verify(userMapper).toUserResponse(any(User.class));
  }


  @Test
  void createUser_UserAlreadyExists_ThrowsException() {
    when(userRepository.existsByUserName(request.getUserName())).thenReturn(true);

    assertThrows(AppEx.class, () -> userService.createUser(request));
  }

  @Test
  void getAllUsers_Success() {
    when(userRepository.findAll()).thenReturn(List.of(user));
    when(userMapper.toUserResponse(any(User.class))).thenReturn(response);

    List<UserResponse> result = userService.getAllUsers();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
  }

  @Test
  void getUserById_UserExists_ReturnsUser() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(userMapper.toUserResponse(any(User.class))).thenReturn(response);

    UserResponse result = userService.getUserById(1L);

    assertNotNull(result);
    assertEquals("ptlinh", result.getUserName());
  }

  @Test
  void getUserById_UserNotFound_ThrowsException() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(AppEx.class, () -> userService.getUserById(1L));
  }


  @Test
  void updateUser_Success() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(roleRepository.findAllById(anySet()))
            .thenReturn(List.of(Role.builder().roleName("HR").build()));
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(userMapper.toUserResponse(any(User.class))).thenReturn(response);

    UserResponse result = userService.updateUser(1L, request);

    assertNotNull(result);
    assertEquals(response.getUserName(), result.getUserName());
    verify(userRepository).findById(1L);
    verify(userRepository).save(any(User.class));
    verify(userMapper).toUserResponse(any(User.class));
  }


  @Test
  void updateUser_UserNotFound_ThrowsException() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(AppEx.class, () -> userService.updateUser(1L, request));
  }

  @Test
  void deleteUser_Success() {
    when(userRepository.existsById(anyLong())).thenReturn(true);
    doNothing().when(userRepository).deleteById(anyLong());

    assertDoesNotThrow(() -> userService.deleteUser(1L));
    verify(userRepository).deleteById(1L);
  }

  @Test
  void deleteUser_UserNotFound_ThrowsException() {
    when(userRepository.existsById(anyLong())).thenReturn(false);

    assertThrows(AppEx.class, () -> userService.deleteUser(1L));
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
    UserResponse userResponse = new UserResponse();
    userResponse.setUserName("***REMOVED***User");
    when(userMapper.toUserResponse(user)).thenReturn(userResponse);

    // Gọi phương thức service
    UserResponse result = userService.getMyInfo();

    // Kiểm tra kết quả
    assertNotNull(result);
    assertEquals("***REMOVED***User", result.getUserName());
    verify(userRepository).findByUserName("***REMOVED***User");
    verify(userMapper).toUserResponse(user);
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
