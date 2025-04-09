package org.training.meetingroombooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.GroupDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.GroupMapper;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.GroupRepository;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    public GroupDTO create(GroupDTO dto) {
        boolean exists = groupRepository.existsById(dto.getGroupName());
        if (exists) {
            throw new AppEx(ErrorCode.GROUP_ALREADY_EXISTS);
        }
        GroupEntity entity = groupMapper.toEntity(dto);
        GroupEntity savedGroup = groupRepository.save(entity);
        return groupMapper.toDTO(savedGroup);
    }


    public List<GroupDTO> getAll() {
        List<GroupEntity> groups = groupRepository.findAll();
        return groups.stream()
                .map(groupMapper::toDTO)
                .toList();
    }

    public Page<GroupDTO> getGroups(String groupName, String location, String division,
                                    int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<GroupEntity> spec = Specification.where(null);
        if (groupName != null && !groupName.isEmpty()) {
            spec = spec.and((***REMOVED***, query, cb) ->
                    cb.like(cb.lower(***REMOVED***.get("groupName")), "%" + groupName.toLowerCase() + "%"));
        }
        if (location != null && !location.isEmpty()) {
            spec = spec.and((***REMOVED***, query, cb) ->
                    cb.like(cb.lower(***REMOVED***.get("location")), "%" + location.toLowerCase() + "%"));
        }
        if (division != null && !division.isEmpty()) {
            spec = spec.and((***REMOVED***, query, cb) ->
                    cb.like(cb.lower(***REMOVED***.get("division")), "%" + division.toLowerCase() + "%"));
        }

        Page<GroupEntity> groupPage = groupRepository.findAll(spec, pageable);
        return groupPage.map(groupMapper::toDTO);
    }

    public GroupDTO getById(String groupName) {
        GroupEntity entity = groupRepository.findById(groupName)
                .orElseThrow(() -> new AppEx(ErrorCode.GROUP_NOT_FOUND));
        return groupMapper.toDTO(entity);
    }

    public GroupDTO update(String groupName, GroupDTO dto) {
        GroupEntity existingGroup = groupRepository.findById(groupName)
                .orElseThrow(() -> new AppEx(ErrorCode.GROUP_NOT_FOUND));
        groupMapper.updateEntity(existingGroup, dto);
        GroupEntity updatedGroup = groupRepository.save(existingGroup);
        return groupMapper.toDTO(updatedGroup);
    }

    public void delete(String groupName) {
        if (!groupRepository.existsById(groupName)) {
            throw new AppEx(ErrorCode.GROUP_NOT_FOUND);
        }
        groupRepository.deleteById(groupName);
    }

}
