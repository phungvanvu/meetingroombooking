package org.training.meetingroombooking.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.training.meetingroombooking.entity.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
  Set<Role> findByRoleNameIn(Set<String> roleNames);
}
