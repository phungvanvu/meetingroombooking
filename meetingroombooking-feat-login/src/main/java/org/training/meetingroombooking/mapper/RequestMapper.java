package org.training.meetingroombooking.mapper;

import org.training.meetingroombooking.dto.UserDTO;
import org.training.meetingroombooking.model.Request;

public interface RequestMapper {
  Request toRequest(UserDTO userDTO);
  void updateRequest(Request request, UserDTO userDTO);
}
