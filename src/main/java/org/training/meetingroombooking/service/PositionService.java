package org.training.meetingroombooking.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.dto.PositionDTO;
import org.training.meetingroombooking.entity.Position;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.exception.ErrorCode;
import org.training.meetingroombooking.mapper.PositionMapper;
import org.training.meetingroombooking.repository.PositionRepository;
@Service
public class PositionService {
  private final PositionRepository positionRepository;
  private final PositionMapper positionMapper;

  public PositionService(PositionRepository positionRepository, PositionMapper positionMapper) {
    this.positionRepository = positionRepository;
    this.positionMapper = positionMapper;
  }
  public PositionDTO create(PositionDTO dto) {
    Position entity = positionMapper.toEntity(dto);
    entity = positionRepository.save(entity);
    return positionMapper.toDTO(entity);
  }
  public List<PositionDTO> getAll() {
    List<Position> positions = positionRepository.findAll();
    return positions.stream().map(positionMapper::toDTO).toList();
  }
  public void deletePosition(int positionId) {
    if (!positionRepository.existsById(positionId)) {
      throw new AppEx(ErrorCode.RESOURCE_NOT_FOUND);
    }
    positionRepository.deleteById(positionId);
  }

}
