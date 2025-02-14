package org.training.meetingroombooking.model;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int userId;

  @Column(nullable = false, unique = true)
  private String userName;

  private String fullName;
  private String department;
  private String email;
  private String password;
  private boolean enabled = true;

  @ManyToOne
  @JoinColumn(name = "group_id")
  private GroupEntity group;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  public int getUserId() { return userId; }
  public void setUserId(int userId) { this.userId = userId; }

  public String getUserName() { return userName; }
  public void setUserName(String userName) { this.userName = userName; }

  public String getFullName() { return fullName; }
  public void setFullName(String fullName) { this.fullName = fullName; }

  public String getDepartment() { return department; }
  public void setDepartment(String department) { this.department = department; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public GroupEntity getGroup() { return group; }
  public void setGroup(GroupEntity group) { this.group = group; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean enabled) { this.enabled = enabled; }

  public Role getRole() { return role; }
  public void setRole(Role role) { this.role = role; }

}
