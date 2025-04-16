package org.training.meetingroombooking.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.training.meetingroombooking.entity.models.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
  Optional<User> findByUserName(String userName);

  Optional<User> findByEmail(String email);

  boolean existsByUserName(String userName);

  boolean existsByEmail(String email);
}
