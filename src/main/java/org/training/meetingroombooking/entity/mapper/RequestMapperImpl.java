package org.training.meetingroombooking.entity.mapper;

import org.springframework.stereotype.Component;
import org.training.meetingroombooking.entity.dto.RequestDTO;
import org.training.meetingroombooking.entity.models.Request;

@Component
public class RequestMapperImpl implements RequestMapper {

    @Override
    public RequestDTO toDTO(Request request) {
        if (request == null) {
            return null;
        }
        return new RequestDTO(
                request.getTitle(),
                request.getLocation(),
                request.getDescription(),
                request.getJobLevel(),
                request.getStatus(),
                request.getApproval(),
                request.getTarget(),
                request.getOnboard(),
                request.getCreatedBy(),
                request.getHrPic(),
                request.getAction()
        );
    }

    @Override
    public Request toEntity(RequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Request request = new Request();
        request.setTitle(dto.getTitle());
        request.setLocation(dto.getLocation());
        request.setDescription(dto.getDescription());
        request.setJobLevel(dto.getJobLevel());
        request.setStatus(dto.getStatus());
        request.setApproval(dto.getApproval());
        request.setTarget(dto.getTarget());
        request.setOnboard(dto.getOnboard());
        request.setAction(dto.getAction());

        return request;
    }

}
