package org.training.meetingroombooking.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "requests")
public class Request {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int requestId;

  private String title;
  private String location;
  private String description;
  private String jobLevel;
  private boolean status;
  private String approval;

  @Column(name = "target_date")
  private LocalDate target;

  @Column(name = "onboard_date")
  private LocalDate onboard;

  @ManyToOne
  @JoinColumn(name = "created_by")
  private User createdBy;

  @ManyToOne
  @JoinColumn(name = "hr_pic")
  private User hrPic;

  private String action;

  public int getRequestId() { return requestId; }
  public void setRequestId(int requestId) { this.requestId = requestId; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public String getJobLevel() { return jobLevel; }
  public void setJobLevel(String jobLevel) { this.jobLevel = jobLevel; }

  public boolean isStatus() { return status; }
  public void setStatus(boolean status) { this.status = status; }

  public String getApproval() { return approval; }
  public void setApproval(String approval) { this.approval = approval; }

  public LocalDate getTarget() { return target; }
  public void setTarget(LocalDate target) { this.target = target; }

  public LocalDate getOnboard() { return onboard; }
  public void setOnboard(LocalDate onboard) { this.onboard = onboard; }

  public User getCreatedBy() { return createdBy; }
  public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

  public User getHrPic() { return hrPic; }
  public void setHrPic(User hrPic) { this.hrPic = hrPic; }

  public String getAction() { return action; }
  public void setAction(String action) { this.action = action; }

}
