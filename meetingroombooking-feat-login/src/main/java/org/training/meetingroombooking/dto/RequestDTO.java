package org.training.meetingroombooking.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import org.training.meetingroombooking.model.User;

public class RequestDTO {
  private String title;
  private String location;
  private String description;
  private String jobLevel;
  private boolean status;
  private String approval;
  private LocalDate target;
  private LocalDate onboard;
  private User createdBy;
  private User hrPic;
  private String action;


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getJobLevel() {
    return jobLevel;
  }

  public void setJobLevel(String jobLevel) {
    this.jobLevel = jobLevel;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public String getApproval() {
    return approval;
  }

  public void setApproval(String approval) {
    this.approval = approval;
  }

  public LocalDate getTarget() {
    return target;
  }

  public void setTarget(LocalDate target) {
    this.target = target;
  }

  public LocalDate getOnboard() {
    return onboard;
  }

  public void setOnboard(LocalDate onboard) {
    this.onboard = onboard;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public User getHrPic() {
    return hrPic;
  }

  public void setHrPic(User hrPic) {
    this.hrPic = hrPic;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }
}
