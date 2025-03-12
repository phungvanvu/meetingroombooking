package org.training.meetingroombooking.service;

import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.GroupDTO;
import org.training.meetingroombooking.entity.mapper.GroupMapper;
import org.training.meetingroombooking.entity.models.GroupEntity;
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
        GroupEntity entity = groupMapper.toEntity(dto);
        entity = groupRepository.save(entity);
        return groupMapper.toDTO(entity);
    }

    public List<GroupDTO> getAll() {
        List<GroupEntity> groups = groupRepository.findAll();
        return groups.stream()
                .map(groupMapper::toDTO)
                .toList();
    }

    public void update(String groupName, GroupDTO dto) {
        GroupEntity entity = groupRepository.findById(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        groupMapper.updateEntity(entity, dto);
        groupRepository.save(entity);
    }
}
