package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.training.meetingroombooking.entity.models.GroupEntity;

@Repository
public interface GroupRepository
    extends JpaRepository<GroupEntity, String>, JpaSpecificationExecutor<GroupEntity> {}
