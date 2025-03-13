package org.training.meetingroombooking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.training.meetingroombooking.entity.dto.PositionDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.PositionMapper;
import org.training.meetingroombooking.entity.models.Position;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PositionRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {

  private PositionRepository positionRepository;
  private PositionMapper positionMapper;

  @Transactional
  public PositionDTO create(PositionDTO dto) {
    Position entity = positionMapper.toEntity(dto);
    entity = positionRepository.save(entity);
    return positionMapper.toDTO(entity);
  }

  @Transactional(readOnly = true)
  public List<PositionDTO> getAll() {
    List<Position> positions = positionRepository.findAll();
    return positions.stream()
            .map(positionMapper::toDTO)
            .toList();
  }

  @Transactional
  public void deletePosition(String positionName) {
    if (!positionRepository.existsByPositionName(positionName)) {
      throw new AppEx(ErrorCode.POSITION_NOT_FOUND);
    }
    positionRepository.deleteByPositionName(positionName);
  }
}
