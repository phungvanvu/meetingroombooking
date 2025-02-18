package org.training.meetingroombooking.dto;

import lombok.Builder;

@Builder
public class positionRequest {
    private int positionId;
    private String positionName;
    private String description;

    public positionRequest(int positionId, String positionName, String description) {
        this.positionId = positionId;
        this.positionName = positionName;
        this.description = description;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
