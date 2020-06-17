package kz.eserzhanov.microservice.auth.auth_microservice.service;

import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Boolean save(RefreshToken refreshToken);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
