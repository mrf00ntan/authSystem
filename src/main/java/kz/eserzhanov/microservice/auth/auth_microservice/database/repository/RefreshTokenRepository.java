package kz.eserzhanov.microservice.auth.auth_microservice.database.repository;

import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.RefreshToken;
import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshTokenAndCreatedGreaterThanEqual(String refreshToken, Date date);
    Optional<RefreshToken> findByUser(User user);
}
