package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {}
