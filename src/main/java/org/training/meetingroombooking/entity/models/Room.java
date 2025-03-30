package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Rooms",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"roomName"})
        })
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long roomId;

  @NotBlank
  @Size(max = 100)
  @Column(nullable = false, length = 100)
  private String roomName;

  @NotBlank
  @Column(nullable = false, length = 255)
  private String location;

  @Column(nullable = false)
  private int capacity;

  @Column(nullable = false)
  private boolean available;

  @ManyToMany
  @JoinTable(
          name = "Room_Equipment",
          joinColumns = @JoinColumn(name = "roomId"),
          inverseJoinColumns = @JoinColumn(name = "equipmentId")
  )
  private Set<Equipment> equipments;

  @Size(max = 255)
  @Column(length = 255)
  private String note;

  @Column(length = 255)
  private String imageUrl;

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RoomBooking> bookings;
}
