package org.training.meetingroombooking.entity.mapper;

import org.training.meetingroombooking.entity.dto.RequestDTO;
import org.training.meetingroombooking.entity.models.Request;

public interface RequestMapper {
    RequestDTO toDTO(Request request);
    Request toEntity(RequestDTO dto);
    void updateEntity(Request request, RequestDTO dto);
}
