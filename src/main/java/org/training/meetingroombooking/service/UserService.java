package org.training.meetingroombooking.service;

import jakarta.persistence.criteria.Join;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.Request.UserRequest;
import org.training.meetingroombooking.entity.dto.Response.UserResponse;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.entity.models.Position;
import org.training.meetingroombooking.entity.models.Role;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.UserMapper;
import org.training.meetingroombooking.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoomBookingRepository roomBookingRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PositionRepository positionRepository;
    private final GroupRepository groupRepository;

  public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       UserMapper userMapper, PositionRepository positionRepository,
                     GroupRepository groupRepository, RoomBookingRepository roomBookingRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder(10);
        this.positionRepository = positionRepository;
        this.groupRepository = groupRepository;
        this.roomBookingRepository = roomBookingRepository;
  }

    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new AppEx(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppEx(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        Position position = positionRepository.findById(request.getPosition())
                .orElseThrow(() -> new AppEx(ErrorCode.POSITION_NOT_FOUND));
        GroupEntity group = groupRepository.findById(request.getGroup())
                .orElseThrow(() -> new AppEx(ErrorCode.GROUP_NOT_FOUND));
        Set<Role> existingRoles = roleRepository.findByRoleNameIn(request.getRoles());
        Set<String> existingRoleNames = existingRoles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
        Set<String> invalidRoles = request.getRoles().stream()
                .filter(role -> !existingRoleNames.contains(role))
                .collect(Collectors.toSet());
        if (!invalidRoles.isEmpty()) {
            throw new AppEx(ErrorCode.ROLE_NOT_FOUND);
        }
        User user = userMapper.toEntity(request);
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new AppEx(ErrorCode.PASSWORD_NULL);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPosition(position);
        user.setGroup(group);
        user.setRoles(new HashSet<>(existingRoles));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    public Page<UserResponse> getUsers(String fullName, String department,
                                       Set<String> positions, Set<String> groups, Set<String> roles, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "userId"));
        Specification<User> spec = Specification.where(null);

        if (fullName != null && !fullName.isEmpty()) {
            spec = spec.and((***REMOVED***, query, cb) ->
                    cb.like(cb.lower(***REMOVED***.get("fullName")), "%" + fullName.toLowerCase() + "%")
            );
        }
        if (department != null && !department.isEmpty()) {
            spec = spec.and((***REMOVED***, query, cb) ->
                    cb.like(cb.lower(***REMOVED***.get("department")), "%" + department.toLowerCase() + "%")
            );
        }
        if (positions != null && !positions.isEmpty()) {
            spec = spec.and((***REMOVED***, query, cb) -> {
                Join<User, Position> positionJoin = ***REMOVED***.join("position");
                return positionJoin.get("positionName").in(positions);
            });
            spec = spec.and((***REMOVED***, query, cb) -> {
                query.distinct(true);
                return cb.conjunction();
            });
        }
        if (groups != null && !groups.isEmpty()) {
            spec = spec.and((***REMOVED***, query, cb) -> {
                Join<User, GroupEntity> groupJoin = ***REMOVED***.join("group");
                return groupJoin.get("groupName").in(groups);
            });
            spec = spec.and((***REMOVED***, query, cb) -> {
                query.distinct(true);
                return cb.conjunction();
            });
        }
        if (roles != null && !roles.isEmpty()) {
            spec = spec.and((***REMOVED***, query, cb) -> {
                Join<User, Role> roleJoin = ***REMOVED***.join("roles");
                return roleJoin.get("roleName").in(roles);
            });
            spec = spec.and((***REMOVED***, query, cb) -> {
                query.distinct(true);
                return cb.conjunction();
            });
        }
        Page<User> usersPage = userRepository.findAll(spec, pageable);
        return usersPage.map(userMapper::toUserResponse);
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
            String[] headers = {"UserName", "FullName",
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
                row.createCell(0).setCellValue(user.getUserName() != null ? user.getUserName() : "N/A");
                row.createCell(1).setCellValue(user.getFullName() != null ? user.getFullName() : "N/A");
                row.createCell(2).setCellValue(user.getEmail() != null ? user.getEmail() : "N/A");
                row.createCell(3).setCellValue(
                        user.getPhoneNumber() != null ? user.getPhoneNumber() : "N/A");
                row.createCell(4).setCellValue(
                        user.getDepartment() != null ? user.getDepartment() : "N/A");
                Set<String> roles = user.getRoles();
                String rolesString = (roles != null && !roles.isEmpty()) ? String.join(", ", roles) : "N/A";
                row.createCell(5).setCellValue(rolesString);
                row.createCell(6).setCellValue(user.getGroupName() != null ? user.getGroupName() : "N/A");
                row.createCell(7).setCellValue(user.getPositionName() != null ? user.getPositionName() : "N/A");
                row.createCell(8).setCellValue(user.isEnabled());
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
                .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppEx(ErrorCode.USER_NOT_FOUND));
        if (request.getUserName() != null &&
                !request.getUserName().equals(user.getUserName()) &&
                userRepository.existsByUserName(request.getUserName())) {
            throw new AppEx(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (request.getPosition() != null) {
            Position position = positionRepository.findById(request.getPosition())
                    .orElseThrow(() -> new AppEx(ErrorCode.POSITION_NOT_FOUND));
            user.setPosition(position);
        }
        if (request.getGroup() != null) {
            GroupEntity group = groupRepository.findById(request.getGroup())
                    .orElseThrow(() -> new AppEx(ErrorCode.GROUP_NOT_FOUND));
            user.setGroup(group);
        }
        userMapper.updateEntity(user, request);
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findByRoleNameIn(request.getRoles()));
            if (roles.isEmpty()) {
                throw new AppEx(ErrorCode.ROLE_NOT_FOUND);
            }
            user.setRoles(roles);
        }
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    public void delete(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new AppEx(ErrorCode.USER_NOT_FOUND);
        }
        User user = userOptional.get();
        if (roomBookingRepository.existsByBookedBy(user)) {
            throw new AppEx(ErrorCode.CANNOT_DELETE_USER_IN_USE);
        }
        userRepository.deleteById(userId);
    }

}
