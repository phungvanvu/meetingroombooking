package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.*;
import org.training.meetingroombooking.entity.dto.GroupDTO;
import org.training.meetingroombooking.entity.models.GroupEntity;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDTO toDTO(GroupEntity entity);

    GroupEntity toEntity(GroupDTO dto);

    void updateEntity(@MappingTarget GroupEntity entity, GroupDTO dto);
}
