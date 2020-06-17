package kz.eserzhanov.microservice.auth.auth_microservice.service;

import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String login);
    Boolean save(User user);
}
