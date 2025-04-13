package org.training.meetingroombooking.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.training.meetingroombooking.entity.dto.GroupDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.GroupMapper;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.entity.models.Position;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.GroupRepository;

import java.util.List;
import org.training.meetingroombooking.repository.UserRepository;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository,
        GroupMapper groupMapper, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.userRepository = userRepository;
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
        Optional<GroupEntity> groupOptional = groupRepository.findById(groupName);
        if (!groupOptional.isPresent()) {
            throw new AppEx(ErrorCode.GROUP_NOT_FOUND);
        }
        GroupEntity group = groupOptional.get();
        if (userRepository.existsByGroup(group)) {
            throw new AppEx(ErrorCode.CANNOT_DELETE_GROUP_IN_USE);
        }
        groupRepository.deleteById(groupName);
    }

    @Transactional
    public void deleteMultipleGroups(List<String> groupName) {
        if (groupName == null || groupName.isEmpty()) {
            throw new AppEx(ErrorCode.INVALID_INPUT);
        }
        List<GroupEntity> groups = groupRepository.findAllById(groupName);
        if (groups.size() != groupName.size()) {
            throw new AppEx(ErrorCode.GROUP_NOT_FOUND);
        }
        for (GroupEntity group : groups) {
            if (userRepository.existsByGroup(group)) {
                throw new AppEx(ErrorCode.CANNOT_DELETE_GROUP_IN_USE);
            }
        }
        groupRepository.deleteAll(groups);
    }

}
