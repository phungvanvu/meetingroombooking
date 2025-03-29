package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String userName;

    @Column(length = 100)
    private String fullName;

    @Column(length = 50)
    private String department;

    @Column(unique = true, length = 50)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "positionId")
    private Position position;

    @Column(nullable = false)
    private String password;

    private boolean enabled = true;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private GroupEntity group;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name")
    )
    private Set<Role> roles;
}
