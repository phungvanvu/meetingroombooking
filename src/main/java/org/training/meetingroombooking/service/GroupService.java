package org.training.meetingroombooking.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.training.meetingroombooking.entity.dto.GroupDTO;

public interface GroupService {
  GroupDTO create(GroupDTO dto);

  List<GroupDTO> getAll();

  Page<GroupDTO> getGroups(
      String groupName,
      String location,
      String division,
      int page,
      int size,
      String sortBy,
      String sortDirection);

  GroupDTO getById(String groupName);

  GroupDTO update(String groupName, GroupDTO dto);

  void delete(String groupName);

  void deleteMultipleGroups(List<String> groupName);
}
