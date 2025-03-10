package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Permissions")
public class Permission {

    @Id
    @Column(nullable = false, unique = true, length = 50)
    private String namePermission;

    @Column(length = 255)
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
}
