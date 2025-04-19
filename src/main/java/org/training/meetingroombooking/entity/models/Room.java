package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rooms")
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

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<RoomEquipment> roomEquipments;

  @Size(max = 255)
  @Column(columnDefinition = "TEXT", length = 255)
  private String note;

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RoomImage> images = new ArrayList<>();

  @OneToMany(mappedBy = "room")
  private List<RoomBooking> bookings;

  public void addImage(RoomImage img) {
    if (images == null) {
      images = new ArrayList<>();
    }
    images.add(img);
    img.setRoom(this);
  }

  public void clearImages() {
    for (RoomImage img : images) {
      img.setRoom(null);
    }
    images.clear();
  }
}
