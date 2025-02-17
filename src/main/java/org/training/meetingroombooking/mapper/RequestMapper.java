package org.training.meetingroombooking.mapper;

import org.training.meetingroombooking.dto.RequestDTO;
import org.training.meetingroombooking.entity.Request;

public interface RequestMapper {
    RequestDTO toDTO(Request request);
    Request toEntity(RequestDTO dto);
}
