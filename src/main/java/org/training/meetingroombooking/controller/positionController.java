package org.training.meetingroombooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.Position;
import org.training.meetingroombooking.repository.positionRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/positions")
public class positionController {
    @Autowired
    private positionRepository positionRepository;

    @GetMapping
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Position getPositionById(@PathVariable int id) {
        for (Position position : positionRepository.findAll()) {
            if (position.getPositionId() == id) {
                return position;
            }
        }
        return null;
    }

    @PostMapping
    public Position createPosition(@RequestBody Position position) {
        return positionRepository.save(position);
    }

    @PutMapping("/{id}")
    public boolean updatePosition(@PathVariable int id,@RequestBody Position positionDetails) {
        for (Position position : positionRepository.findAll()) {
            if (position.getPositionId() == id) {
                position.setPositionName(positionDetails.getPositionName());
                position.setDescription(positionDetails.getDescription());
                positionRepository.save(position);
                return true;
            }
        }
        return false;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable int id) {
        if (positionRepository.existsById(id)) {
            positionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

