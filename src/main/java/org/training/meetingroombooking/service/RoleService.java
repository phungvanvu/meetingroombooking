package org.training.meetingroombooking.service;

import java.util.List;
import org.training.meetingroombooking.entity.dto.RoleDTO;

public interface RoleService {
  RoleDTO create(RoleDTO request);

  List<RoleDTO> getAll();

  RoleDTO update(String roleName, RoleDTO request);

  void delete(String roleName);
}
