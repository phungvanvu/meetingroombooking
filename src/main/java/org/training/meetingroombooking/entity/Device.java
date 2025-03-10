package org.training.meetingroombooking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "devices")
public class Device {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int deviceId;

  private String deviceName;

  @ManyToOne
  @JoinColumn(name = "room_id")
  private Room room;

}
