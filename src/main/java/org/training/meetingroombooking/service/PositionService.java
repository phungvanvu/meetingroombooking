package org.training.meetingroombooking.service;

import org.springframework.stereotype.Service;
import org.training.meetingroombooking.entity.dto.PositionDTO;
import org.training.meetingroombooking.entity.enums.ErrorCode;
import org.training.meetingroombooking.entity.mapper.PositionMapper;
import org.training.meetingroombooking.entity.models.Position;
import org.training.meetingroombooking.exception.AppEx;
import org.training.meetingroombooking.repository.PositionRepository;

import java.util.List;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
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

    public PositionDTO update(String positionName, PositionDTO dto) {
        Position position = positionRepository.findById(positionName)
                .orElseThrow(() -> new AppEx(ErrorCode.POSITION_NOT_FOUND));
        positionMapper.updatePosition(position, dto);
        Position updatedPosition = positionRepository.save(position);
        return positionMapper.toDTO(updatedPosition);
    }

    public void deletePosition(String positionName) {
        if (!positionRepository.existsById(positionName)) {
            throw new AppEx(ErrorCode.POSITION_NOT_FOUND);
        }
        positionRepository.deleteById(positionName);
    }
}
