package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.training.meetingroombooking.entity.models.Position;

public interface PositionRepository
    extends JpaRepository<Position, String>, JpaSpecificationExecutor<Position> {}
