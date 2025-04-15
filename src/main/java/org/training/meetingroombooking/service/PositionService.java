package org.training.meetingroombooking.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.training.meetingroombooking.entity.dto.PositionDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.PositionMapper;
import org.training.meetingroombooking.entity.models.Position;
import org.training.meetingroombooking.entity.models.User;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PositionRepository;

import java.util.List;
import org.training.meetingroombooking.repository.UserRepository;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final UserRepository userRepository;
    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository,
        PositionMapper positionMapper, UserRepository userRepository) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
        this.userRepository = userRepository;
    }

    public PositionDTO create(PositionDTO dto) {
        boolean exists = positionRepository.existsById(dto.getPositionName());
        if (exists) {
            throw new AppEx(ErrorCode.POSITION_ALREADY_EXISTS);
        }
        Position entity = positionMapper.toEntity(dto);
        Position savedPosition = positionRepository.save(entity);
        return positionMapper.toDTO(savedPosition);
    }


    public List<PositionDTO> getAll() {
        List<Position> positions = positionRepository.findAll();
        return positions.stream()
                .map(positionMapper::toDTO)
                .toList();
    }

    public Page<PositionDTO> getPositions(String positionName, String description,
                                          int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Position> spec = Specification.where(null);
        if (positionName != null && !positionName.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("positionName")), "%" + positionName.toLowerCase() + "%"));
        }
        if (description != null && !description.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
        }
        Page<Position> positionPage = positionRepository.findAll(spec, pageable);
        return positionPage.map(positionMapper::toDTO);
    }

    public PositionDTO update(String positionName, PositionDTO dto) {
        Position position = positionRepository.findById(positionName)
                .orElseThrow(() -> new AppEx(ErrorCode.POSITION_NOT_FOUND));
        positionMapper.updatePosition(position, dto);
        Position updatedPosition = positionRepository.save(position);
        return positionMapper.toDTO(updatedPosition);
    }

    public void deletePosition(String positionName) {
        Optional<Position> positionOptional = positionRepository.findById(positionName);
        if (!positionOptional.isPresent()) {
            throw new AppEx(ErrorCode.POSITION_NOT_FOUND);
        }
        Position position = positionOptional.get();
//        if (userRepository.existsByPosition(position)) {
//            throw new AppEx(ErrorCode.CANNOT_DELETE_POSITION_IN_USE);
//        }
        positionRepository.delete(position);
    }

    @Transactional
    public void deleteMultiplePositions(List<String> positionName) {
        if (positionName == null || positionName.isEmpty()) {
            throw new AppEx(ErrorCode.INVALID_INPUT);
        }
        List<Position> positions = positionRepository.findAllById(positionName);
        if (positions.size() != positionName.size()) {
            throw new AppEx(ErrorCode.POSITION_NOT_FOUND);
        }
//        for (Position position : positions) {
//            if (userRepository.existsByPosition(position)) {
//                throw new AppEx(ErrorCode.CANNOT_DELETE_POSITION_IN_USE);
//            }
//        }
        positionRepository.deleteAll(positions);
    }

}
