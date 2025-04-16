package org.training.meetingroombooking.entity.dto.Response;

import org.training.meetingroombooking.exception.ApiError;

public class ApiResponse<T> {
  private boolean success;
  private T data;
  private ApiError error;

  public ApiResponse(Builder<T> builder) {
    this.success = builder.success;
    this.data = builder.data;
    this.error = builder.error;
  }

  public static <T> Builder<T> builder() {
    return new Builder<>();
  }

  public static <T> ApiResponse<T> success(T data) {
    return ApiResponse.<T>builder().success(true).data(data).build();
  }

  public static ApiResponse<?> error(ApiError error) {
    return ApiResponse.builder().success(false).error(error).build();
  }

  public static class Builder<T> {
    private boolean success;
    private T data;
    private ApiError error;

    public Builder<T> success(boolean success) {
      this.success = success;
      return this;
    }

    public Builder<T> data(T data) {
      this.data = data;
      return this;
    }

    public Builder<T> error(ApiError error) {
      this.error = error;
      return this;
    }

    public ApiResponse<T> build() {
      return new ApiResponse<>(this);
    }
  }

  public boolean isSuccess() {
    return success;
  }

  public T getData() {
    return data;
  }

  public ApiError getError() {
    return error;
  }
}
