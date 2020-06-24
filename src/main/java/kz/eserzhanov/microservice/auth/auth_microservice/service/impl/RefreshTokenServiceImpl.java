package kz.eserzhanov.microservice.auth.auth_microservice.service.impl;

import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.RefreshToken;
import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.User;
import kz.eserzhanov.microservice.auth.auth_microservice.database.repository.RefreshTokenRepository;
import kz.eserzhanov.microservice.auth.auth_microservice.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.token.expiration.seconds}")
    private long EXPIRATION_TIME_IN_SECONDS;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    @Override
    public Boolean save(RefreshToken refreshToken) {
        deleteExists(refreshToken.getUser());

        return refreshTokenRepository.save(refreshToken).getId() != null;
    }

    @Transactional
    @Override
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshTokenAndCreatedGreaterThanEqual(refreshToken, new Date(System.currentTimeMillis() - EXPIRATION_TIME_IN_SECONDS * 1000)).orElse(null);
    }

    private void deleteExists(User user){
        Optional<RefreshToken> optional = refreshTokenRepository.findByUser(user);
        if(optional.isPresent()){
            refreshTokenRepository.delete(optional.get());
            refreshTokenRepository.flush();
        }
    }
}
