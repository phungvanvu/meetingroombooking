package org.training.meetingroombooking.model;

public class Resource {
private long resourceId;
private String resourceName;
private String email;
private String phone;
private String StepProcess;
private String Create;
private String Assignee;
private String action;
public  Resource() {}
    public  Resource(long resourceId, String resourceName, String email, String phone, String stepProcess, String create, String assignee) {
    this.resourceId = resourceId;
    this.resourceName = resourceName;
    this.email = email;
    this.phone = phone;
    this.StepProcess = stepProcess;
    this.Create = create;
    this.Assignee = assignee;
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
    }
}

