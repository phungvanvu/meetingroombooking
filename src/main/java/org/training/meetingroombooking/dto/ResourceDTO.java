package org.training.meetingroombooking.dto;

public class ResourceDTO {
    private long resourceId;
    private String resourceName;
    private String email;
    private String phone;
    private String stepProcess;
    private String create;
    private String assignee;
    private String action;

    public ResourceDTO(long resourceId, String resourceName, String email, String phone,
                       String stepProcess, String create, String assignee, String action) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.email = email;
        this.phone = phone;
        this.stepProcess = stepProcess;
        this.create = create;
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
        return stepProcess;
    }

    public void setStepProcess(String stepProcess) {
        this.stepProcess = stepProcess;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
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
