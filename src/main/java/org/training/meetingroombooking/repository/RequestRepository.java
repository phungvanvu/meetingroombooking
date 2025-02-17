package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}
