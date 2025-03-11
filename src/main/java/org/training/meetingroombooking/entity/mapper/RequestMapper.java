package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.dto.RequestDTO;

public interface RequestMapper {
    RequestDTO toDTO(Request request);
    Request toEntity(RequestDTO dto);
}
