package org.training.meetingroombooking.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.model.Request;

public interface RequestRepository extends JpaRepository<Request, Integer> {
  Optional<Request> findByUserName(String userName);
  boolean isexist(Request request);

}
