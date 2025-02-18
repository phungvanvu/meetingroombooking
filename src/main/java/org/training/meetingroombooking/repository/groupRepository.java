package org.training.meetingroombooking.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.Group;

import java.util.Optional;

public interface groupRepository extends JpaRepository<Group, Id> {
    Optional<Object> findById(int id);
    void deleteById(int id);
}
