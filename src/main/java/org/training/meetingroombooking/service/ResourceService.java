package org.training.meetingroombooking.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.ResourceDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.ResourceMapper;
import org.training.meetingroombooking.entity.models.Resource;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.ResourceRepository;

@Service
public class ResourceService {

  private final ResourceRepository resourceRepository;
  private final ResourceMapper resourceMapper;

  public ResourceService(ResourceRepository resourceRepository, ResourceMapper resourceMapper) {
    this.resourceRepository = resourceRepository;
    this.resourceMapper = resourceMapper;
  }

  public ResourceDTO create(ResourceDTO dto) {
    Resource resource = resourceMapper.toEntity(dto);
    Resource saveResource = resourceRepository.save(resource);
    return resourceMapper.toDTO(saveResource);
  }

  public ResourceDTO getById(Long resourceId) {
    Optional<Resource> resource = resourceRepository.findById(resourceId);
    return resource.map(resourceMapper::toDTO).orElseThrow(
        () -> new AppEx(ErrorCode.RESOURCE_NOT_FOUND));
  }

  public List<ResourceDTO> getAll() {
    return resourceRepository.findAll()
        .stream()
        .map(resourceMapper::toDTO)
        .toList();
  }

  public ResourceDTO update(Long resourceId, ResourceDTO resourceDTO) {
    Optional<Resource> existingResource = resourceRepository.findById(resourceId);
    if (existingResource.isPresent()) {
      Resource resource = existingResource.get();
      resourceMapper.updateEntity(resource, resourceDTO);
      Resource updateResource = resourceRepository.save(resource);
      return resourceMapper.toDTO(updateResource);
    }
    throw new AppEx(ErrorCode.RESOURCE_NOT_FOUND);
  }

  public void delete(Long resourceId) {
    if(!resourceRepository.existsById(resourceId)) {
      throw new AppEx(ErrorCode.RESOURCE_NOT_FOUND);
    }
    resourceRepository.deleteById(resourceId);
  }

}
