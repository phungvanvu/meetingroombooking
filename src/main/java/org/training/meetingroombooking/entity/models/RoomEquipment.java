package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "room_Equipment")
public class RoomEquipment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "roomId", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Room room;

  @ManyToOne
  @JoinColumn(name = "equipmentName", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Equipment equipment;
}
