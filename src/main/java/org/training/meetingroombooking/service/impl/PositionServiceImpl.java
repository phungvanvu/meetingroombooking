package org.training.meetingroombooking.service.impl;

import java.util.List;
import java.util.stream.Collectors;
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
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PositionRepository;
import org.training.meetingroombooking.service.PositionService;

@Service
public class PositionServiceImpl implements PositionService {

  private final PositionRepository positionRepository;
  private final PositionMapper positionMapper;

  public PositionServiceImpl(PositionRepository positionRepository, PositionMapper positionMapper) {
    this.positionRepository = positionRepository;
    this.positionMapper = positionMapper;
  }

  @Override
  public PositionDTO create(PositionDTO dto) {
    if (positionRepository.existsById(dto.getPositionName())) {
      throw new AppEx(ErrorCode.POSITION_ALREADY_EXISTS);
    }
    Position entity = positionMapper.toEntity(dto);
    Position savedPosition = positionRepository.save(entity);
    return positionMapper.toDTO(savedPosition);
  }

  @Override
  public List<PositionDTO> getAll() {
    return positionRepository.findAll().stream()
        .map(positionMapper::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public Page<PositionDTO> getPositions(
      String positionName,
      String description,
      int page,
      int size,
      String sortBy,
      String sortDirection) {
    Pageable pageable = PageRequest.of(page, size, getSort(sortBy, sortDirection));
    Specification<Position> spec = createSearchSpecification(positionName, description);
    Page<Position> positionPage = positionRepository.findAll(spec, pageable);
    return positionPage.map(positionMapper::toDTO);
  }

  private Sort getSort(String sortBy, String sortDirection) {
    return Sort.by(
        sortDirection.equalsIgnoreCase("DESC") ? Sort.Order.desc(sortBy) : Sort.Order.asc(sortBy));
  }

  private Specification<Position> createSearchSpecification(
      String positionName, String description) {
    Specification<Position> spec = Specification.where(null);
    if (positionName != null && !positionName.isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) ->
                  cb.like(
                      cb.lower(root.get("positionName")), "%" + positionName.toLowerCase() + "%"));
    }
    if (description != null && !description.isEmpty()) {
      spec =
          spec.and(
              (root, query, cb) ->
                  cb.like(
                      cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
    }
    return spec;
  }

  @Override
  public PositionDTO update(String positionName, PositionDTO dto) {
    Position position =
        positionRepository
            .findById(positionName)
            .orElseThrow(() -> new AppEx(ErrorCode.POSITION_NOT_FOUND));

    positionMapper.updatePosition(position, dto);
    Position updatedPosition = positionRepository.save(position);
    return positionMapper.toDTO(updatedPosition);
  }

  @Override
  public void deletePosition(String positionName) {
    positionRepository
        .findById(positionName)
        .ifPresentOrElse(
            positionRepository::delete,
            () -> {
              throw new AppEx(ErrorCode.POSITION_NOT_FOUND);
            });
  }

  @Override
  @Transactional
  public void deleteMultiplePositions(List<String> positionNames) {
    if (positionNames == null || positionNames.isEmpty()) {
      throw new AppEx(ErrorCode.INVALID_INPUT);
    }
    List<Position> positions = positionRepository.findAllById(positionNames);
    if (positions.size() != positionNames.size()) {
      throw new AppEx(ErrorCode.POSITION_NOT_FOUND);
    }
    positionRepository.deleteAll(positions);
  }
}
