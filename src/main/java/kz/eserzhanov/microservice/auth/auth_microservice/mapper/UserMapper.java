package kz.eserzhanov.microservice.auth.auth_microservice.mapper;

import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.User;
import kz.eserzhanov.microservice.auth.auth_microservice.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {
        })
public abstract class UserMapper {
        public abstract UserDto fromUser(User entity);
        public abstract User toUser(UserDto dto);
}
