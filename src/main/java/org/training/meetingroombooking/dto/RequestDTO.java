package org.training.meetingroombooking.dto;

import jakarta.validation.constraints.Size;
import org.training.meetingroombooking.entity.User;

import java.time.LocalDate;

public class RequestDTO {
    @Size(min = 1, max = 100, message = "title cannot be empty")
    private String title;

    @Size(min = 1, max = 100)
    private String location;

    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;

    @Size(min = 1, max = 255)
    private String jobLevel;

    private boolean status;

    @Size(min = 1, max = 100)
    private String approval;

    private LocalDate target;
    private LocalDate onboard;
    private User createdBy;
    private User hrPic;

    @Size(min = 1, max = 255)
    private String action;

    public RequestDTO() {}

    public RequestDTO(String title, String location, String description, String jobLevel,
                      boolean status, String approval, LocalDate target, LocalDate onboard,
                      User createdBy, User hrPic, String action) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.jobLevel = jobLevel;
        this.status = status;
        this.approval = approval;
        this.target = target;
        this.onboard = onboard;
        this.createdBy = createdBy;
        this.hrPic = hrPic;
        this.action = action;
    }

    public static class Builder {
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

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder jobLevel(String jobLevel) {
            this.jobLevel = jobLevel;
            return this;
        }

        public Builder status(boolean status) {
            this.status = status;
            return this;
        }

        public Builder approval(String approval) {
            this.approval = approval;
            return this;
        }

        public Builder target(LocalDate target) {
            this.target = target;
            return this;
        }

        public Builder onboard(LocalDate onboard) {
            this.onboard = onboard;
            return this;
        }

        public Builder createdBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder hrPic(User hrPic) {
            this.hrPic = hrPic;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }
        public RequestDTO build() {
            return new RequestDTO(title, location, description, jobLevel,
                    status, approval, target, onboard, createdBy, hrPic, action);
        }

    }

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
