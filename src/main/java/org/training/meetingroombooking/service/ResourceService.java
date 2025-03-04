package org.training.meetingroombooking.service;

import org.springframework.stereotype.Service;
import org.training.meetingroombooking.dto.ResourceDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class ResourceService {
    private List<ResourceDTO> resources = new ArrayList<>();
    public void addResource(ResourceDTO resource) {
        resources.add(resource);
    }
    public List<ResourceDTO> getAllResources() {
        return resources;
    }
    public Optional<ResourceDTO> getResourceById(long resourceId) {
        return resources.stream()
                .filter(resource -> resource.getResourceId() == resourceId)
                .findFirst();
    }
    public boolean updateResource(long resourceId, ResourceDTO updatedResource) {
        Optional<ResourceDTO> resourceOptional = getResourceById(resourceId);
        if (resourceOptional.isPresent()) {
            ResourceDTO resource = resourceOptional.get();
            resource.setResourceName(updatedResource.getResourceName());
            resource.setEmail(updatedResource.getEmail());
            resource.setPhone(updatedResource.getPhone());
            resource.setStepProcess(updatedResource.getStepProcess());
            resource.setCreate(updatedResource.getCreate());
            resource.setAssignee(updatedResource.getAssignee());
            resource.setAction(updatedResource.getAction());
            return true;
        }
        return false;
    }

    public boolean deleteResource(long resourceId) {
        return resources.removeIf(resource -> resource.getResourceId() == resourceId);
    }
    public List<ResourceDTO> filterResources(String name, String phone, String skill) {
        return resources.stream()
                .filter(resource -> (name == null || resource.getResourceName().contains(name)) &&
                        (phone == null || resource.getPhone().contains(phone)) &&
                        (skill == null || resource.getStepProcess().contains(skill)))
                .collect(Collectors.toList());
    }
    public List<ResourceDTO> getResourcesWithPagination(int page, int size) {
        int start = page * size;
        int end = Math.min(start + size, resources.size());
        return resources.subList(start, end);
    }
    public boolean validateResource(ResourceDTO resource) {
        return resource.getEmail().matches("") &&
                resource.getPhone().matches("") &&
                !resource.getResourceName().isEmpty() &&
                !resource.getStepProcess().isEmpty();
    }
}