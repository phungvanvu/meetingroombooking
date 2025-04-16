package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Permissions")
public class Permission {

  @Id
  @Column(nullable = false, unique = true, length = 50)
  private String permissionName;

  @Column(columnDefinition = "TEXT", length = 255)
  private String description;
}
