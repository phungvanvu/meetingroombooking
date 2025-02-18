package org.training.meetingroombooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.Group;
import org.training.meetingroombooking.repository.groupRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
public class groupController {
    @Autowired
    private groupRepository groupRepository;

    @GetMapping
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable int id) {
        for (Group group : groupRepository.findAll()) {
            if (group.getGroupId() == id) {
                return group;
            }
        }
        return null;
    }

    @PostMapping
    public Group createGroup(Group group) {
        groupRepository.save(group);
        return group;
    }

    @PutMapping("/{id}")
    public boolean updateGroup(int id, Group groupDetails) {
        for (Group group : groupRepository.findAll()) {
            if (group.getGroupId() == id) {
                group.setGroupName(groupDetails.getGroupName());
                group.setLocation(groupDetails.getLocation());
                group.setDivision(groupDetails.getDivision());
                group.setDepartment(groupDetails.getDepartment());
                group.setStatus(groupDetails.isStatus());
                groupRepository.save(group);
                return true;
            }
        }
        return false;
    }

    @DeleteMapping("/{id}")
    public String deleteGroup(@PathVariable int id) {
        groupRepository.deleteById(id);
        return "deleted";
    }
}
