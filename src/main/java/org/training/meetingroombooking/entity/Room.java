package org.training.meetingroombooking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "Rooms")
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int roomId;

  @Column(nullable = false, unique = true)
  private String roomName;

  @Column(nullable = false)
  private int roomCapacity;

  private String location;

  private boolean status;

  private String image;

}

