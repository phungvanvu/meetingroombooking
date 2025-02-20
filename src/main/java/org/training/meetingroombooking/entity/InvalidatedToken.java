package org.training.meetingroombooking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "invalidated_token")
public class InvalidatedToken {
    @Id
    private String id;
    private Date expiryTime;

    public InvalidatedToken() {
    }

    public InvalidatedToken(String id, Date expiryTime) {
        this.id = id;
        this.expiryTime = expiryTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public static class Builder {
        private String id;
        private Date expiryTime;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder expiryTime(Date expiryTime) {
            this.expiryTime = expiryTime;
            return this;
        }

        public InvalidatedToken build() {
            return new InvalidatedToken(id, expiryTime);
        }
    }

    public static Builder builder() {
        return new Builder();
    }


//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        InvalidatedToken that = (InvalidatedToken) o;
//        return Objects.equals(id, that.id);
//    }

//    public int hashCode() {
//        return Objects.hash(id);
//    }
}
