package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.training.meetingroombooking.entity.dto.RequestDTO;
import org.training.meetingroombooking.entity.models.Request;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface RequestMapper {

    @Mapping(target = "createdBy", source = "createdBy.userId")
    @Mapping(target = "hrPic", source = "hrPic.userId")
    RequestDTO toDTO(Request entity);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapUserIdToUser")
    @Mapping(target = "hrPic", source = "hrPic", qualifiedByName = "mapUserIdToUser")
    Request toEntity(RequestDTO dto);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapUserIdToUser")
    @Mapping(target = "hrPic", source = "hrPic", qualifiedByName = "mapUserIdToUser")
    void updateEntity(@MappingTarget Request entity, RequestDTO dto);


    @Named("mapRequestIdToRequest")
    default Request mapRequestIdToRequest(Long requestId) {
        if (requestId == null) {
            return null;
        }
        Request request = new Request();
        request.setRequestId(requestId);
        return request;
    }
}

