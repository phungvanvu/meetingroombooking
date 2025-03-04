package org.training.meetingroombooking.mapper;

import org.training.meetingroombooking.dto.ResourceDTO;
import org.training.meetingroombooking.entity.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapperImpl implements ResourceMapper {

    @Override
    public ResourceDTO toDTO(Resource resource) {
        if (resource == null) {
            return null;
        }
        return new ResourceDTO(
                resource.getResourceId(),
                resource.getResourceName(),
                resource.getEmail(),
                resource.getPhone(),
                resource.getStepProcess(),
                resource.getCreate(),
                resource.getAssignee(),
                resource.getAction()
        );
    }

    @Override
    public Resource updateEntity(Resource resource, ResourceDTO resourceDTO) {
        if (resource == null || resourceDTO == null) {
            return null;
        }
        resource.setResourceName(resourceDTO.getResourceName());
        resource.setEmail(resourceDTO.getEmail());
        resource.setPhone(resourceDTO.getPhone());
        resource.setStepProcess(resourceDTO.getStepProcess());
        resource.setCreate(resourceDTO.getCreate());
        resource.setAssignee(resourceDTO.getAssignee());
        resource.setAction(resourceDTO.getAction());

        return resource;
    }
}