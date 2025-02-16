package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.training.meetingroombooking.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
