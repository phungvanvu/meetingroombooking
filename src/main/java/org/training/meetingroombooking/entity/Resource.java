package org.training.meetingroombooking.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "resources")
public class Resource {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
  private long resourceId;
  private String resourceName;
  private String email;
  private String phone;
  private String StepProcess;
  private String CreateResource;
  private String assignee;
  private String action;
  public Resource() {}
  public Resource(long resourceId, String resourceName, String email, String phone,
                  String stepProcess, String create, String assignee, String action) {
    this.resourceId = resourceId;
    this.resourceName = resourceName;
    this.email = email;
    this.phone = phone;
    this.StepProcess = stepProcess;
    this.CreateResource = create;
    this.assignee = assignee;
    this.action = action;
  }
  public long getResourceId() {
    return resourceId;
  }

  public void setResourceId(long resourceId) {
    this.resourceId = resourceId;
  }

  public String getResourceName() {
    return resourceName;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getStepProcess() {
    return StepProcess;
  }

  public void setStepProcess(String stepProcess) {
    this.StepProcess = stepProcess;
  }

  public String getCreate() {
    return CreateResource;
  }

  public void setCreate(String create) {
    this.CreateResource = create;
  }

  public String getAssignee() {
    return assignee;
  }

  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }
}