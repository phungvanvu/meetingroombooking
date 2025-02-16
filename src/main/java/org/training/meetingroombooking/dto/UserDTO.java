package org.training.meetingroombooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.training.meetingroombooking.entity.GroupEntity;
import java.util.Set;

public class UserDTO {
    @Size(min = 3, message = "UserName must be at least 3 characters")
    private String userName;

    @NotNull
    private String fullName;

    private String department;

    @Email
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private boolean enabled = true;
    private GroupEntity group;

    private Set<String> roles;

    public UserDTO() {}

    public UserDTO(String userName, String fullName, String department, String email, String password, boolean enabled, GroupEntity group, Set<String> roles) {
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

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder group(GroupEntity group) {
            this.group = group;
            return this;
        }

        public Builder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(userName, fullName, department, email, password, enabled, group, roles);
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
        this.fullName = fullName;
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
