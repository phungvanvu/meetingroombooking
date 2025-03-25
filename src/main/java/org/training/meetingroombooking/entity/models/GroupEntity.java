package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`groups`")
public class GroupEntity {

    @Id
    @Column(nullable = false, length = 50)
    private String groupName;

    @Column(length = 100)
    private String location;

    @Column(length = 50)
    private String division;

    @Column(length = 50)
    private String department;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
