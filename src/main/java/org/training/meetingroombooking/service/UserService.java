package org.training.meetingroombooking.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.training.meetingroombooking.entity.dto.Request.ChangePasswordRequest;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;

public interface UserService {
  UserResponse createUser(UserRequest request);

  Page<UserResponse> getUsers(
      String fullName,
      String department,
      Set<String> positions,
      Set<String> groups,
      Set<String> roles,
      int page,
      int size);

  List<UserResponse> getAll();

  ByteArrayOutputStream exportUserToExcel() throws IOException;

  UserResponse getById(Long userId);

  UserResponse getMyInfo();

  UserResponse update(Long userId, UserRequest request);

  void delete(Long userId);

  void deleteMultipleUsers(List<Long> userIds);

  void changePassword(ChangePasswordRequest request);
}
