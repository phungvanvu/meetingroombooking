package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.training.meetingroombooking.entity.dto.ResourceDTO;
import org.training.meetingroombooking.entity.models.Resource;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ResourceMapper {

  @Mapping(target = "createdById", source = "createdBy.userId")
  @Mapping(target = "assigneeId", source = "assignee.userId")
  ResourceDTO toDTO(Resource entity);

  @Mapping(target = "createdBy", source = "createdById", qualifiedByName = "mapUserIdToUser")
  @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "mapUserIdToUser")
  Resource toEntity(ResourceDTO dto);

  @Mapping(target = "createdBy", source = "createdById", qualifiedByName = "mapUserIdToUser")
  @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "mapUserIdToUser")
  void updateEntity(@MappingTarget Resource entity, ResourceDTO dto);
}
