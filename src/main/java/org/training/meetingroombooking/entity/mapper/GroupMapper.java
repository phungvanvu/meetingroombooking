package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.training.meetingroombooking.entity.dto.GroupDTO;
import org.training.meetingroombooking.entity.models.GroupEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupDTO toDTO(GroupEntity entity);

    GroupEntity toEntity(GroupDTO dto);

    void updateEntity(@MappingTarget GroupEntity entity, GroupDTO dto);
}
