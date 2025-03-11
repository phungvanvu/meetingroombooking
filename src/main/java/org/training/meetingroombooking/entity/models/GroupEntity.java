package org.training.meetingroombooking.entity.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate createdDate;
}
