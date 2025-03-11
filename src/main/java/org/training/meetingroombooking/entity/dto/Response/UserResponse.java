package org.training.meetingroombooking.entity.dto.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

  private Long userId;

  private String userName;

  private String fullName;

  private String department;

  private String email;

  private String phoneNumber;

  private String positionName;

  private String groupName;

  private Set<String> roles;

  private boolean enabled;
}