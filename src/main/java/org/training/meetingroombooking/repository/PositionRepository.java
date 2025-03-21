package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.training.meetingroombooking.entity.models.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, String> {
}
