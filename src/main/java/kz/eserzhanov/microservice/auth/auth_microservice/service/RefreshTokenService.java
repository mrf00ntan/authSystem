package kz.eserzhanov.microservice.auth.auth_microservice.service;

import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.RefreshToken;

public interface RefreshTokenService {
    Boolean save(RefreshToken refreshToken);
    RefreshToken findByRefreshToken(String refreshToken);
}
