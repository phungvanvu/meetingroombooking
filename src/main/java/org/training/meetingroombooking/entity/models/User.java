package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false, length = 50, unique = true)
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
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private Position position;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean enabled;

  @ManyToOne
  @JoinColumn(name = "groupId")
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private GroupEntity group;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Notification> notifications = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_name"))
  private Set<Role> roles;
}
