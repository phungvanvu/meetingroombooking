package org.training.meetingroombooking.exception;

import org.training.meetingroombooking.entity.enums.ErrorCode;

public class AppEx extends RuntimeException {
    private final ErrorCode errorCode;

    public AppEx(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}

