package org.training.meetingroombooking.entity.dto.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import org.training.meetingroombooking.entity.models.GroupEntity;

public class UserRequest {

  @NotNull(message = "UserName cannot be null")
  @Size(min = 3, max = 50, message = "UserName must be between 3 and 50 characters")
  @Pattern(regexp = "^[^\\s]+$", message = "Username and password should not contain any spaces")
  private String userName;


  @NotNull(message = "FullName cannot be null")
  @Size(min = 1, max = 50, message = "FullName cannot be empty")
  private String fullName;

  @Size(min = 1, max = 75, message = "department must be between 1 and 75 characters")
  private String department;

  @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
      message = "Invalid email format. Please provide a valid email address with proper domain (e.g., user@example.com).")
  @Size(max = 100, message = "Email cannot exceed 100 characters")
  private String email;

  @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
  @Pattern(regexp = "^[^\\s]+$", message = "Username and password should not contain any spaces")
  private String password;

  private boolean enabled = true;

  private GroupEntity group;

  private Set<String> roles;

  public UserRequest() {}

  public UserRequest(String userName, String fullName, String department, String email, String password, boolean enabled, GroupEntity group, Set<String> roles) {
    this.userName = userName;
    this.fullName = fullName;
    this.department = department;
    this.email = email;
    this.password = password;
    this.enabled = enabled;
    this.group = group;
    this.roles = roles;
  }

  public static class Builder {
    private String userName;
    private String fullName;
    private String department;
    private String email;
    private String password;
    private boolean enabled = true;
    private GroupEntity group;
    private Set<String> roles;

    public UserRequest.Builder userName(String userName) {
      this.userName = userName;
      return this;
    }

    public UserRequest.Builder fullName(String fullName) {
      this.fullName = fullName;
      return this;
    }

    public UserRequest.Builder department(String department) {
      this.department = department;
      return this;
    }

    public UserRequest.Builder email(String email) {
      this.email = email;
      return this;
    }

    public UserRequest.Builder password(String password) {
      this.password = password;
      return this;
    }

    public UserRequest.Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    public UserRequest.Builder group(GroupEntity group) {
      this.group = group;
      return this;
    }

    public UserRequest.Builder roles(Set<String> roles) {
      this.roles = roles;
      return this;
    }

    public UserRequest build() {
      return new UserRequest(userName, fullName, department, email, password, enabled, group, roles);
    }
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName != null ? fullName.trim() : null;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public GroupEntity getGroup() {
    return group;
  }

  public void setGroup(GroupEntity group) {
    this.group = group;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }


}
