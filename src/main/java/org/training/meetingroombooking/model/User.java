package org.training.meetingroombooking.model;

public class User {
  private int userId;
  private String userName;
  private String fullName;
  private String department;
  private String email;
  private int groupId;
  private int roleId;

  public User() {}

  public User(int userId, String userName, String fullName, String department, String email, int groupId, int roleId) {
    this.userId = userId;
    this.userName = userName;
    this.fullName = fullName;
    this.department = department;
    this.email = email;
    this.groupId = groupId;
    this.roleId = roleId;
  }
  public int getUserId() {return userId;}
  public String getUserName() {return userName;}


}
