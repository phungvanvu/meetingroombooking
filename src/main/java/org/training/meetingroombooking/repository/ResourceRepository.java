package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.models.Resource;

public interface ResourceRepository extends JpaRepository<Resource,Long> {

}
