package kz.eserzhanov.microservice.auth.auth_microservice.service;

import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.User;
import kz.eserzhanov.microservice.auth.auth_microservice.dto.UserDto;
import kz.eserzhanov.microservice.auth.auth_microservice.pojo.AuthRequestPOJO;
import kz.eserzhanov.microservice.auth.auth_microservice.pojo.AuthResponsePOJO;

public interface UserService {
    User findByLogin(String login);
    Boolean save(User user);

    AuthResponsePOJO signUp(AuthRequestPOJO request);
    AuthResponsePOJO register(UserDto userDto);
    AuthResponsePOJO refresh(String refreshToken);
}
