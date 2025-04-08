package org.training.meetingroombooking.entity.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.training.meetingroombooking.exception.ApiError;

@Getter
public enum ErrorCode {
    // Các lỗi 400 Bad Request
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "Invalid file type. Only PNG, JPG, and JPEG are allowed."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation error occurred."),
    PASSWORD_NULL(HttpStatus.BAD_REQUEST, "Password must be provided."),
    EQUIPMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "Equipment already exists."),
    GROUP_ALREADY_EXISTS(HttpStatus.CONFLICT, "Group already exists."),
    POSITION_ALREADY_EXISTS(HttpStatus.CONFLICT, "Position already exists."),
    ROLE_EXISTS(HttpStatus.CONFLICT, "Role already exists"),
    PERMISSION_ALREADY_EXISTS(HttpStatus.CONFLICT, "Permission already exists."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "UserName already exists."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists."),
    ALREADY_BOOKED(HttpStatus.CONFLICT, "This room is already booked for the selected time."),
    BATCH_CANCELLATION_FAILED(HttpStatus.CONFLICT, "Some bookings could not be cancelled."),

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "Invalid input."),

    // Các lỗi 401 Unauthorized
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "Incorrect username or password."),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "User is not authenticated."),

    // Các lỗi 404 Not Found
    ROOM_BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "Room booking not found"),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "Room not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found."),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Role not found."),
    EQUIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Equipment not found"),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Notification not found"),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "Group not found"),
    POSITION_NOT_FOUND(HttpStatus.NOT_FOUND, "Position not found"),
    PERMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "Permission not found"),
    CANNOT_DELETE_USER_IN_USE(HttpStatus.CONFLICT, "Cannot delete user: User is associated with existing bookings."),
    CANNOT_DELETE_ROOM_IN_USE(HttpStatus.CONFLICT, "Cannot delete room: Room is associated with existing bookings."),

    // Lỗi 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiError toApiError(String customMessage) {
        return new ApiError(customMessage);
    }
}
