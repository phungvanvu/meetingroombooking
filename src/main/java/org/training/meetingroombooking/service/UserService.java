package org.training.meetingroombooking.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.UserMapper;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.RoleRepository;
import org.training.meetingroombooking.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, RoleRepository roleRepository,
      UserMapper userMapper) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = new BCryptPasswordEncoder(10);
  }

  public UserResponse createUser(UserRequest request) {
    if (userRepository.existsByUserName(request.getUserName())) {
      throw new AppEx(ErrorCode.USER_ALREADY_EXISTS);
    }

    User user = userMapper.toEntity(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Set<Role> roles = new HashSet<>(roleRepository.findByRoleNameIn(request.getRoles()));
    if (roles.isEmpty()) {
      throw new AppEx(ErrorCode.ROLE_NOT_FOUND);
    }

    user.setRoles(roles);
    User savedUser = userRepository.save(user);
    return userMapper.toUserResponse(savedUser);
  }

  public List<UserResponse> getAll() {
    return userRepository.findAll()
        .stream()
        .map(userMapper::toUserResponse)
        .toList();
  }

  public ByteArrayOutputStream exportUserToExcel() throws IOException {
    List<UserResponse> userResponses = getAll();

    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      Sheet sheet = workbook.createSheet("Users");
      // Tạo hàng tiêu đề
      Row headerRow = sheet.createRow(0);
      String[] headers = {"User ID", "UserName", "FullName",
          "Email", "Phone", "Department", "Roles", "Group", "Position", "isEnabled"};
      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        cell.setCellStyle(style);
      }
      int rowNum = 1;
      for (UserResponse user : userResponses) {
        Row row = sheet.createRow(rowNum++);
        if (user.getUserId() != null) {
          row.createCell(0).setCellValue(user.getUserId());
        } else {
          row.createCell(0).setCellValue("N/A");
        }
        row.createCell(1).setCellValue(user.getUserName() != null ? user.getUserName() : "N/A");
        row.createCell(2).setCellValue(user.getFullName() != null ? user.getFullName() : "N/A");
        row.createCell(3).setCellValue(user.getEmail() != null ? user.getEmail() : "N/A");
        row.createCell(4).setCellValue(
            user.getPhoneNumber() != null ? user.getPhoneNumber() : "N/A");
        row.createCell(5).setCellValue(
            user.getDepartment() != null ? user.getDepartment(): "N/A");
        Set<String> roles = user.getRoles();
        String rolesString = (roles != null && !roles.isEmpty()) ? String.join(", ", roles) : "N/A";
        row.createCell(6).setCellValue(rolesString);
        row.createCell(7).setCellValue(user.getGroupName() != null ? user.getGroupName() : "N/A");
        row.createCell(8).setCellValue(user.getPositionName() != null ? user.getPositionName() : "N/A");
        row.createCell(9).setCellValue(user.isEnabled());
      }
      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
      }
      workbook.write(outputStream);
      return outputStream;
    }
  }

  public UserResponse getById(long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppEx(ErrorCode.RESOURCE_NOT_FOUND));
    return userMapper.toUserResponse(user);
  }

  public UserResponse getMyInfo() {
    var context = SecurityContextHolder.getContext();
    String name = context.getAuthentication().getName();

    User user = userRepository.findByUserName(name).orElseThrow(
        () -> new AppEx(ErrorCode.USER_NOT_FOUND));
    return userMapper.toUserResponse(user);
  }

  public UserResponse update(Long userId, UserRequest request) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new AppEx(ErrorCode.USER_NOT_FOUND));
    userMapper.updateEntity(user, request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    var roles = roleRepository.findAllById(request.getRoles());
    user.setRoles(new HashSet<>(roles));
    return userMapper.toUserResponse(userRepository.save(user));
  }

  public void delete(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new AppEx(ErrorCode.RESOURCE_NOT_FOUND);
    }
    userRepository.deleteById(userId);
  }
}
