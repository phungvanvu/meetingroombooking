package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Roles")
public class Role {

  @Id
  @Column(nullable = false, unique = true, length = 50)
  private String roleName;

  @Column(length = 255)
  private String description;

  @ManyToMany
  @JoinTable(
      name = "role_permissions",
      joinColumns = @JoinColumn(name = "role_name"),
      inverseJoinColumns = @JoinColumn(name = "permission_name"))
  private Set<Permission> permissions;
}
