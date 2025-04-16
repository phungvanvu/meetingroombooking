package org.training.meetingroombooking.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
  Set<Role> findByRoleNameIn(Set<String> roleNames);
}
