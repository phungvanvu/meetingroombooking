package org.training.meetingroombooking.service;

import java.util.List;
import org.training.meetingroombooking.entity.dto.PermissionDTO;

public interface PermissionService {
  PermissionDTO create(PermissionDTO permissionDTO);

  List<PermissionDTO> getAll();

  PermissionDTO update(String permissionName, PermissionDTO dto);

  void delete(String permission);
}
