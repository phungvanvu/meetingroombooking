package org.training.meetingroombooking.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.training.meetingroombooking.entity.dto.NotificationDTO;
import org.training.meetingroombooking.entity.models.Notification;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface NotificationMapper {

  @Mapping(source = "userId", target = "user.userId")
  Notification toEntity(NotificationDTO dto);

  @Mapping(source = "user", target = "userId", qualifiedByName = "mapUserToUserId")
  NotificationDTO toDTO(Notification entity);

}


