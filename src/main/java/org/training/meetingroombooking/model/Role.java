package org.training.meetingroombooking.model;

public class Role {
  private int roleId;
  private String roleName;

  public Role() {}

  public Role(int roleId, String roleName) {
    this.roleId = roleId;
    this.roleName = roleName;
  }

  public int getRoleId() { return roleId; }

  public void setRoleId(int roleId) { this.roleId = roleId; }

  public String getroleName() { return roleName; }

  public void setroleName(String roleName) { this.roleName = roleName; }
}
