package org.training.meetingroombooking.exception;

public enum ErrorCode {
    USER_ALREADY_EXISTS(101, "User already exists."),
    RESOURCE_NOT_FOUND(404, "Requested resource does not exist."),
    FUNCTION_NOT_FOUND(103, "Requested API function does not exist."),
    USAGE_LIMIT_EXCEEDED(104, "Your monthly usage limit has been reached. Please upgrade your subscription plan."),
    FEATURE_NOT_SUPPORTED(105, "Your current subscription plan does not support this function."),
    QUERY_NO_RESULTS(106, "Your query did not return any results."),
    ACCOUNT_INACTIVE(102, "Your account is not active. Contact Customer Support."),
    USER_NOT_FOUND(107, "User not found."),
    INVALID_CREDENTIALS(108, "Invalid username or password."),
    ROLE_NOT_FOUND(109, "Role not found."),
    INVALID_CURRENCY(201, "Invalid source currency."),
    INVALID_CURRENCY_CODE(202, "Invalid currency codes provided."),
    INVALID_DATE(302, "Invalid date format."),
    INVALID_TIME_FRAME(504, "The specified time frame is invalid."),
    TIME_FRAME_TOO_LONG(505, "The time frame exceeds 365 days."),
    VALIDATION_ERROR(400, "Validation error occurred."),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    UNAUTHENTICATED(401, "User is not authenticated.");


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }

    public String getMessage() {
        return message;
    }

    public ApiError toApiError(String customMessage) {
        return new ApiError(this.code, customMessage);
    }
}
