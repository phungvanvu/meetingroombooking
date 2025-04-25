package org.training.meetingroombooking.service.impl;

import jakarta.persistence.criteria.Join;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.training.meetingroombooking.entity.dto.Request.ChangePasswordRequest;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.UserMapper;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.entity.models.Position;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.*;
import org.training.meetingroombooking.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PositionRepository positionRepository;
  private final GroupRepository groupRepository;
  private final RoomBookingRepository roomBookingRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final Validator validator;

  public UserServiceImpl(
      UserRepository userRepository,
      RoleRepository roleRepository,
      UserMapper userMapper,
      PositionRepository positionRepository,
      GroupRepository groupRepository,
      RoomBookingRepository roomBookingRepository,
      Validator validator) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.userMapper = userMapper;
    this.positionRepository = positionRepository;
    this.groupRepository = groupRepository;
    this.roomBookingRepository = roomBookingRepository;
    this.validator = validator;
    this.passwordEncoder = new BCryptPasswordEncoder(10);
  }

  @Override
  public UserResponse createUser(UserRequest request) {
    if (userRepository.existsByUserName(request.getUserName())) {
      throw new AppEx(ErrorCode.USERNAME_ALREADY_EXISTS);
    }
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new AppEx(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    if (request.getPassword() == null || request.getPassword().isBlank()) {
      throw new AppEx(ErrorCode.PASSWORD_NULL);
    }
    Position position =
        positionRepository
            .findById(request.getPosition())
            .orElseThrow(() -> new AppEx(ErrorCode.POSITION_NOT_FOUND));
    GroupEntity group =
        groupRepository
            .findById(request.getGroup())
            .orElseThrow(() -> new AppEx(ErrorCode.GROUP_NOT_FOUND));
    Set<Role> roles = roleRepository.findByRoleNameIn(request.getRoles());
    if (roles.isEmpty()
        || !roles.stream()
            .map(Role::getRoleName)
            .collect(Collectors.toSet())
            .containsAll(request.getRoles())) {
      throw new AppEx(ErrorCode.ROLE_NOT_FOUND);
    }
    User user = userMapper.toEntity(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setPosition(position);
    user.setGroup(group);
    user.setRoles(roles);
    return userMapper.toUserResponse(userRepository.save(user));
  }

  @Override
  public Page<UserResponse> getUsers(
      String fullName,
      String department,
      Set<String> positions,
      Set<String> groups,
      Set<String> roles,
      int page,
      int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "userId"));
    Specification<User> spec = Specification.where(null);
    if (fullName != null && !fullName.isBlank()) {
      spec =
          spec.and(
              (root, query, cb) ->
                  cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%"));
    }
    if (department != null && !department.isBlank()) {
      spec =
          spec.and(
              (root, query, cb) ->
                  cb.like(cb.lower(root.get("department")), "%" + department.toLowerCase() + "%"));
    }
    spec = addJoinFilter(spec, positions, "position", "positionName");
    spec = addJoinFilter(spec, groups, "group", "groupName");
    spec = addJoinFilter(spec, roles, "roles", "roleName");

    return userRepository.findAll(spec, pageable).map(userMapper::toUserResponse);
  }

  private Specification<User> addJoinFilter(
      Specification<User> spec, Set<String> values, String attribute, String fieldName) {
    if (values == null || values.isEmpty()) {
      return spec;
    }
    return spec.and(
        (root, query, cb) -> {
          @SuppressWarnings("unchecked")
          Join<Object, Object> join = root.join(attribute);
          query.distinct(true);
          return join.get(fieldName).in(values);
        });
  }

  @Override
  public List<UserResponse> getAll() {
    return userRepository.findAll().stream()
        .map(userMapper::toUserResponse)
        .collect(Collectors.toList());
  }

  /**
   * Import users from an Excel file. Reads into DTOs for validation, then creates users in a transaction.
   */
  @Override
  @Transactional
  public void importUsersFromExcel(MultipartFile file) {
    List<UserRequest> dtos = new ArrayList<>();
    try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
      Sheet sheet = workbook.getSheetAt(0);
      Iterator<Row> rows = sheet.iterator();
      if (!rows.hasNext()) {
        throw new AppEx(ErrorCode.VALIDATION_ERROR, "Excel file is empty");
      }
      Map<String, Integer> headerMap = mapHeader(rows.next());
      while (rows.hasNext()) {
        Row row = rows.next();
        UserRequest dto = mapRowToDto(row, headerMap);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
          String msg = violations.stream()
                  .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                  .collect(Collectors.joining(", "));
          throw new AppEx(ErrorCode.VALIDATION_ERROR, msg);
        }
        dtos.add(dto);
      }
    } catch (IOException e) {
      throw new AppEx(ErrorCode.VALIDATION_ERROR, "Failed to read Excel: " + e.getMessage());
    }
    for (UserRequest dto : dtos) {
      createUser(dto);
    }
  }

  @Override
  public ByteArrayOutputStream exportUserToExcel() throws IOException {
    List<UserResponse> users = getAll();
    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      Sheet sheet = workbook.createSheet("Users");
      String[] headers = {
        "UserName",
        "FullName",
        "Email",
        "Phone",
        "Department",
        "Roles",
        "Group",
        "Position",
        "Enabled"
      };
      Row headerRow = sheet.createRow(0);
      CellStyle headerStyle = workbook.createCellStyle();
      Font font = workbook.createFont();
      font.setBold(true);
      headerStyle.setFont(font);

      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerStyle);
      }

      int rowNum = 1;
      for (UserResponse u : users) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(u.getUserName());
        row.createCell(1).setCellValue(u.getFullName());
        row.createCell(2).setCellValue(u.getEmail());
        row.createCell(3).setCellValue(u.getPhoneNumber());
        row.createCell(4).setCellValue(u.getDepartment());
        row.createCell(5).setCellValue(String.join(", ", u.getRoles()));
        row.createCell(6).setCellValue(u.getGroupName());
        row.createCell(7).setCellValue(u.getPositionName());
        row.createCell(8).setCellValue(u.isEnabled());
      }

      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
      }

      workbook.write(out);
      return out;
    }
  }

  @Override
  public UserResponse getById(Long userId) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
    return userMapper.toUserResponse(user);
  }

  @Override
  public UserResponse getMyInfo() {
    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
    User user =
        userRepository
            .findByUserName(currentUser)
            .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
    return userMapper.toUserResponse(user);
  }

  @Override
  public UserResponse update(Long userId, UserRequest request) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
    if (request.getUserName() != null
        && !request.getUserName().equals(user.getUserName())
        && userRepository.existsByUserName(request.getUserName())) {
      throw new AppEx(ErrorCode.USERNAME_ALREADY_EXISTS);
    }
    if (request.getPosition() != null) {
      Position pos =
          positionRepository
              .findById(request.getPosition())
              .orElseThrow(() -> new AppEx(ErrorCode.POSITION_NOT_FOUND));
      user.setPosition(pos);
    }
    if (request.getGroup() != null) {
      GroupEntity grp =
          groupRepository
              .findById(request.getGroup())
              .orElseThrow(() -> new AppEx(ErrorCode.GROUP_NOT_FOUND));
      user.setGroup(grp);
    }
    userMapper.updateEntity(user, request);
    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }
    if (request.getRoles() != null && !request.getRoles().isEmpty()) {
      Set<Role> rs = roleRepository.findByRoleNameIn(request.getRoles());
      if (rs.isEmpty()) {
        throw new AppEx(ErrorCode.ROLE_NOT_FOUND);
      }
      user.setRoles(rs);
    }
    return userMapper.toUserResponse(userRepository.save(user));
  }

  @Override
  public void delete(Long userId) {
    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
    User user =
        userRepository.findById(userId).orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));

    if (user.getUserName().equals(currentUser)) {
      throw new AppEx(ErrorCode.CANNOT_DELETE_CURRENT_USER);
    }
    if (roomBookingRepository.existsByBookedBy(user)) {
      throw new AppEx(ErrorCode.CANNOT_DELETE_USER_IN_USE);
    }
    userRepository.deleteById(userId);
  }

  @Override
  public void deleteMultipleUsers(List<Long> userIds) {
    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
    List<User> users = userRepository.findAllById(userIds);
    if (users.size() != userIds.size()) {
      throw new AppEx(ErrorCode.USER_NOT_FOUND);
    }
    List<User> usersToDelete = new ArrayList<>();
    List<String> currentUsers = new ArrayList<>();
    List<String> usersWithBookings = new ArrayList<>();
    for (User u : users) {
      if (u.getUserName().equals(currentUser)) {
        currentUsers.add(u.getUserName());
      } else if (roomBookingRepository.existsByBookedBy(u)) {
        usersWithBookings.add(u.getUserName());
      } else {
        usersToDelete.add(u);
      }
    }
    if (!usersToDelete.isEmpty()) {
      userRepository.deleteAll(usersToDelete);
    }
    if (!currentUsers.isEmpty() || !usersWithBookings.isEmpty()) {
      StringBuilder errorMessage = new StringBuilder("Could not delete the following users: ");
      if (!currentUsers.isEmpty()) {
        errorMessage.append("Your account (").append(String.join(", ", currentUsers)).append(") ");
      }

      if (!usersWithBookings.isEmpty()) {
        if (!currentUsers.isEmpty()) {
          errorMessage.append("and ");
        }
        errorMessage
            .append("Users with active bookings: ")
            .append(String.join(", ", usersWithBookings));
      }
      throw new AppEx(ErrorCode.PARTIAL_USER_DELETE_FAILED, errorMessage.toString());
    }
  }

  @Override
  public void changePassword(ChangePasswordRequest request) {
    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
    User user =
        userRepository
            .findByUserName(currentUser)
            .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
      throw new AppEx(ErrorCode.INVALID_PASSWORD);
    }
    if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
      throw new AppEx(ErrorCode.NEW_PASSWORD_MUST_BE_DIFFERENT);
    }
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
  }

  /**
   * Map header row to column index by header name (case-insensitive).
   */
  private Map<String, Integer> mapHeader(Row headerRow) {
    Map<String, Integer> map = new HashMap<>();
    DataFormatter fmt = new DataFormatter();
    for (Cell cell : headerRow) {
      String header = fmt.formatCellValue(cell).trim().toLowerCase();
      map.put(header, cell.getColumnIndex());
    }
    return map;
  }

  /**
   * Convert an Excel row into a UserRequest DTO.
   */
  private UserRequest mapRowToDto(Row row, Map<String, Integer> headerMap) {
    DataFormatter fmt = new DataFormatter();
    Function<String, String> getCellValue = key -> {
      Integer idx = headerMap.get(key.toLowerCase());
      return idx == null ? null : fmt.formatCellValue(row.getCell(idx)).trim();
    };
    String rolesStr = getCellValue.apply("roles");
    Set<String> roles = (rolesStr == null || rolesStr.isEmpty()) ?
            Collections.emptySet() :
            Arrays.stream(rolesStr.split("\\s*,\\s*"))
                    .collect(Collectors.toSet());
    return UserRequest.builder()
            .userName(getCellValue.apply("username"))
            .fullName(getCellValue.apply("fullname"))
            .department(getCellValue.apply("department"))
            .email(getCellValue.apply("email"))
            .phoneNumber(getCellValue.apply("phonenumber"))
            .password(getCellValue.apply("password"))
            .enabled(Boolean.parseBoolean(getCellValue.apply("enabled")))
            .position(getCellValue.apply("position"))
            .group(getCellValue.apply("group"))
            .roles(roles)
            .build();
  }
}
