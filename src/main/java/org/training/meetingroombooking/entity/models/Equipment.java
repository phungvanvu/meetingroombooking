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
@Table(name = "equipments")
public class Equipment {

  @Id
  @Column(nullable = false, length = 50)
  private String equipmentName;

  @Column(columnDefinition = "TEXT", length = 255)
  private String description;

  @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<RoomEquipment> roomEquipments;
}
