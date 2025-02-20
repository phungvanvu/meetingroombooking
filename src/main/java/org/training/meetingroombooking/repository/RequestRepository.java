package org.training.meetingroombooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.training.meetingroombooking.entity.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Page<Request> findAll(Pageable pageable);
    List<Request> findByTitle(String title);
    List<Request> findByTitleContainingIgnoreCase(String keyword);
    Page<Request> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
