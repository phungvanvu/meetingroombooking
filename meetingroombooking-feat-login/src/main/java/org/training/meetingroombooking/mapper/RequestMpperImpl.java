package org.training.meetingroombooking.mapper;

import org.training.meetingroombooking.dto.RequestDTO;
import org.training.meetingroombooking.dto.UserDTO;
import org.training.meetingroombooking.model.Request;

public class RequestMpperImpl {

  public RequestMpperImpl() {}

  public void updateRequest(Request request, RequestDTO requestDTO) {
    if(requestDTO != null && request != null) {
      request.setTitle(request.getTitle());
      request.setLocation(request.getLocation());
      request.setDescription(request.getDescription());
    }
  }
}
