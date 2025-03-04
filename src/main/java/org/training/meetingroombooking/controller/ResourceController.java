package org.training.meetingroombooking.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.training.meetingroombooking.dto.ResourceDTO;
import org.training.meetingroombooking.service.ResourceService;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/resource")
public class ResourceController {
    private ResourceService resourceService;
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }
    @PostMapping
    public void addResource(ResourceDTO resource) {
        resourceService.addResource(resource);
    }
    public List<ResourceDTO> getAllResources() {
        return resourceService.getAllResources();
    }
    public Optional<ResourceDTO> getResourceById(long resourceId) {
        return resourceService.getResourceById(resourceId);
    }
    public boolean updateResource(long resourceId, ResourceDTO updatedResource) {
        return resourceService.updateResource(resourceId, updatedResource);
    }
    public boolean deleteResource(long resourceId) {
        return resourceService.deleteResource(resourceId);
    }
    public List<ResourceDTO> filterResources(String name, String phone, String status) {
        return resourceService.filterResources(name, phone, status);
    }
}
