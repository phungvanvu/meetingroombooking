package org.training.meetingroombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.training.meetingroombooking.entity.models.GroupEntity;
import org.training.meetingroombooking.entity.models.Position;
import org.training.meetingroombooking.entity.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByPosition(Position position);

    boolean existsByGroup(GroupEntity group);
}
