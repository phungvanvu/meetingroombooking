package org.training.meetingroombooking.entity.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
