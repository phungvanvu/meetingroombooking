package org.training.meetingroombooking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

  public int getId() { return resourceId; }
  public void setId(int id) { this.resourceId = resourceId; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  public String getStepProcess() { return stepProcess; }
  public void setStepProcess(String stepProcess) { this.stepProcess = stepProcess; }

  public User getCreatedBy() { return createdBy; }
  public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

  public User getAssignee() { return assignee; }
  public void setAssignee(User assignee) { this.assignee = assignee; }

  public String getAction() { return action; }
  public void setAction(String action) { this.action = action; }
}
