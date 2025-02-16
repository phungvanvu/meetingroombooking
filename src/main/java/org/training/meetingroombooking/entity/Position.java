package org.training.meetingroombooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "positions")
public class Position {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int positionId;

  @Column(nullable = false, unique = true)
  private String positionName;

  @Column(columnDefinition = "TEXT")
  private String description;

  public int getPositionId() { return positionId; }
  public void setPositionId(int positionId) { this.positionId = positionId; }

  public String getPositionName() { return positionName; }
  public void setPositionName(String positionName) { this.positionName = positionName; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
}
