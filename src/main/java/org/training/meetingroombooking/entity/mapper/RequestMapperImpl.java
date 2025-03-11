package org.training.meetingroombooking.entity.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.training.meetingroombooking.entity.dto.RequestDTO;
import org.training.meetingroombooking.entity.models.Request;
import org.training.meetingroombooking.repository.UserRepository;

@Component
public class RequestMapperImpl implements RequestMapper {

    @Autowired
    private UserRepository userRepository;

    @Override
    public RequestDTO toDTO(Request request) {
        if (request == null) {
            return null;
        }
        return RequestDTO.builder()
                .title(request.getTitle())
                .location(request.getLocation())
                .description(request.getDescription())
                .jobLevel(request.getJobLevel())
                .status(request.getStatus())
                .approval(request.getApproval())
                .target(request.getTarget())
                .onboard(request.getOnboard())
                .createdBy(request.getCreatedBy() != null ? request.getCreatedBy().getUserId() : null)
                .hrPic(request.getHrPic() != null ? request.getHrPic().getUserId() : null)
                .action(request.getAction())
                .build();
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

        // Xử lý createdBy và hrPic nếu có ID hợp lệ
        if (dto.getCreatedBy() != null) {
            request.setCreatedBy(userRepository.findById(dto.getCreatedBy()).orElse(null));
        }
        if (dto.getHrPic() != null) {
            request.setHrPic(userRepository.findById(dto.getHrPic()).orElse(null));
        }

        return request;
    }

    @Override
    public void updateEntity(Request request, RequestDTO dto) {
        if (request == null || dto == null) {
            return;
        }

        if (dto.getTitle() != null) request.setTitle(dto.getTitle());
        if (dto.getLocation() != null) request.setLocation(dto.getLocation());
        if (dto.getDescription() != null) request.setDescription(dto.getDescription());
        if (dto.getJobLevel() != null) request.setJobLevel(dto.getJobLevel());
        if (dto.getStatus() != null) request.setStatus(dto.getStatus());
        if (dto.getApproval() != null) request.setApproval(dto.getApproval());
        if (dto.getTarget() != null) request.setTarget(dto.getTarget());
        if (dto.getOnboard() != null) request.setOnboard(dto.getOnboard());
        if (dto.getAction() != null) request.setAction(dto.getAction());

        if (dto.getCreatedBy() != null) {
            request.setCreatedBy(userRepository.findById(dto.getCreatedBy()).orElse(null));
        }
        if (dto.getHrPic() != null) {
            request.setHrPic(userRepository.findById(dto.getHrPic()).orElse(null));
        }
    }
}
