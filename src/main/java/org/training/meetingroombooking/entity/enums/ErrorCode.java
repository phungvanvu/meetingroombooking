package org.training.meetingroombooking.entity.enums;

import org.training.meetingroombooking.exception.ApiError;

public enum ErrorCode {
    USER_ALREADY_EXISTS("User already exists."),
    RESOURCE_NOT_FOUND("Requested resource does not exist."),
    FUNCTION_NOT_FOUND("Requested API function does not exist."),
    USAGE_LIMIT_EXCEEDED("Your monthly usage limit has been reached. Please upgrade your subscription plan."),
    FEATURE_NOT_SUPPORTED("Your current subscription plan does not support this function."),
    QUERY_NO_RESULTS("Your query did not return any results."),
    ACCOUNT_INACTIVE("Your account is not active. Contact Customer Support."),
    USER_NOT_FOUND("User not found."),
    INVALID_CREDENTIALS("Invalid username or password."),
    ROLE_NOT_FOUND("Role not found."),
    INVALID_CURRENCY("Invalid source currency."),
    INVALID_CURRENCY_CODE("Invalid currency codes provided."),
    INVALID_DATE("Invalid date format."),
    INVALID_TIME_FRAME("The specified time frame is invalid."),
    TIME_FRAME_TOO_LONG("The time frame exceeds 365 days."),
    VALIDATION_ERROR("Validation error occurred."),
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    UNAUTHENTICATED("User is not authenticated.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ApiError toApiError(String customMessage) {
        return new ApiError(customMessage);
    }
}
