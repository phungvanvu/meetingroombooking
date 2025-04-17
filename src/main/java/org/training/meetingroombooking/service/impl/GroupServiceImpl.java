package org.training.meetingroombooking.service.impl;

import java.util.List;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.training.meetingroombooking.entity.dto.GroupDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.GroupMapper;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.GroupRepository;
import org.training.meetingroombooking.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

  private final GroupRepository groupRepository;
  private final GroupMapper groupMapper;

  public GroupServiceImpl(GroupRepository groupRepository, GroupMapper groupMapper) {
    this.groupRepository = groupRepository;
    this.groupMapper = groupMapper;
  }

  @Override
  public GroupDTO create(GroupDTO dto) {
    if (groupRepository.existsById(dto.getGroupName())) {
      throw new AppEx(ErrorCode.GROUP_ALREADY_EXISTS);
    }
    GroupEntity entity = groupMapper.toEntity(dto);
    return groupMapper.toDTO(groupRepository.save(entity));
  }

  @Override
  public List<GroupDTO> getAll() {
    return groupRepository.findAll().stream().map(groupMapper::toDTO).toList();
  }

  @Override
  public Page<GroupDTO> getGroups(
      String groupName,
      String location,
      String division,
      int page,
      int size,
      String sortBy,
      String sortDirection) {
    Sort sort =
        sortDirection.equalsIgnoreCase("DESC")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);

    Specification<GroupEntity> spec = Specification.where(null);
    if (groupName != null && !groupName.isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) ->
                  cb.like(cb.lower(root.get("groupName")), "%" + groupName.toLowerCase() + "%"));
    }
    if (location != null && !location.isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) ->
                  cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
    }
    if (division != null && !division.isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) ->
                  cb.like(cb.lower(root.get("division")), "%" + division.toLowerCase() + "%"));
    }

    return groupRepository.findAll(spec, pageable).map(groupMapper::toDTO);
  }

  @Override
  public GroupDTO getById(String groupName) {
    GroupEntity entity =
        groupRepository.findById(groupName).orElseThrow(() -> new AppEx(ErrorCode.GROUP_NOT_FOUND));
    return groupMapper.toDTO(entity);
  }

  @Override
  public GroupDTO update(String groupName, GroupDTO dto) {
    GroupEntity entity =
        groupRepository.findById(groupName).orElseThrow(() -> new AppEx(ErrorCode.GROUP_NOT_FOUND));
    groupMapper.updateEntity(entity, dto);
    return groupMapper.toDTO(groupRepository.save(entity));
  }

  @Override
  public void delete(String groupName) {
    GroupEntity entity =
        groupRepository.findById(groupName).orElseThrow(() -> new AppEx(ErrorCode.GROUP_NOT_FOUND));
    groupRepository.delete(entity);
  }

  @Override
  @Transactional
  public void deleteMultipleGroups(List<String> groupNames) {
    if (groupNames == null || groupNames.isEmpty()) {
      throw new AppEx(ErrorCode.INVALID_INPUT);
    }

    List<GroupEntity> groups = groupRepository.findAllById(groupNames);
    if (groups.size() != groupNames.size()) {
      throw new AppEx(ErrorCode.GROUP_NOT_FOUND);
    }

    groupRepository.deleteAll(groups);
  }
}
