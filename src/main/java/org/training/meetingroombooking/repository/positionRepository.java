package org.training.meetingroombooking.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.Position;

import java.util.Optional;

public interface positionRepository extends JpaRepository<Position, Id> {
    Optional<Object> fingById(int id);

    boolean existsById(int id);

    void deleteById(int id);

    Optional<Position> findById(int id);
}
