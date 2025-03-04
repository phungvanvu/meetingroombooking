package org.training.meetingroombooking.mapper;

import org.training.meetingroombooking.dto.ResourceDTO;
import org.training.meetingroombooking.entity.Resource;
import org.springframework.stereotype.Component;
@Component
public interface ResourceMapper {
    ResourceDTO toDTO(Resource resource);
    Resource updateEntity(Resource resource, ResourceDTO resourceDTO);
}
