package org.training.meetingroombooking.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    private String namePermission;

    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    public Permission() {
    }

    public Permission(String namePermission, String description, Set<Role> roles) {
        this.namePermission = namePermission;
        this.description = description;
        this.roles = roles;
    }

    public String getNamePermission() {
        return namePermission;
    }

    public void setNamePermission(String namePermission) {
        this.namePermission = namePermission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public static class Builder {
        private String namePermission;
        private String description;
        private Set<Role> roles;

        public Builder name(String namePermission) {
            this.namePermission = namePermission;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Permission build() {
            return new Permission(namePermission, description, roles);
        }
    }
}
