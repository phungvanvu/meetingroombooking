package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Integer> {

}
