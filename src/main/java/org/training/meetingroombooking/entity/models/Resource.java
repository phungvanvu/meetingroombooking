package org.training.meetingroombooking.entity.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "resources")
public class Resource {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int resourceId;

  private String name;
  private String email;
  private String phone;
  private String stepProcess;

  @ManyToOne
  @JoinColumn(name = "created_by")
  private User createdBy;

  @ManyToOne
  @JoinColumn(name = "assignee_id")
  private User assignee;

  private String action;
}
