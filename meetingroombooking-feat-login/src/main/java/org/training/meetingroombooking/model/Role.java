package org.training.meetingroombooking.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int roleId;

  private String roleName;

  @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<User> users;

  public int getRoleId() { return roleId; }
  public void setRoleId(int roleId) { this.roleId = roleId; }

  public String getRoleName() { return roleName; }
  public void setRoleName(String roleName) { this.roleName = roleName; }

  public List<User> getUsers() { return users; }
  public void setUsers(List<User> users) { this.users = users; }

}
