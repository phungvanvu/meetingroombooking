package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.training.meetingroombooking.entity.dto.ResourceDTO;
import org.training.meetingroombooking.entity.models.Resource;

@Mapper(componentModel = "spring")
public interface ResourceMapper {
  ResourceDTO toDTO(Resource entity);

  Resource toEntiry(ResourceDTO dto);
}
