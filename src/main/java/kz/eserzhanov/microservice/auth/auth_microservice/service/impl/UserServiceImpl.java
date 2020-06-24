package kz.eserzhanov.microservice.auth.auth_microservice.service.impl;

import kz.eserzhanov.microservice.auth.auth_microservice.config.jwt.JWTAuthorizationFilter;
import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.RefreshToken;
import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.User;
import kz.eserzhanov.microservice.auth.auth_microservice.database.exception.PermissionDeniedException;
import kz.eserzhanov.microservice.auth.auth_microservice.database.exception.UserLoginFoundException;
import kz.eserzhanov.microservice.auth.auth_microservice.database.exception.UserNotFoundException;
import kz.eserzhanov.microservice.auth.auth_microservice.database.repository.UserRepository;
import kz.eserzhanov.microservice.auth.auth_microservice.dto.UserDto;
import kz.eserzhanov.microservice.auth.auth_microservice.mapper.UserMapper;
import kz.eserzhanov.microservice.auth.auth_microservice.pojo.AuthRequestPOJO;
import kz.eserzhanov.microservice.auth.auth_microservice.pojo.AuthResponsePOJO;
import kz.eserzhanov.microservice.auth.auth_microservice.service.RefreshTokenService;
import kz.eserzhanov.microservice.auth.auth_microservice.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, RefreshTokenService refreshTokenService, JWTAuthorizationFilter jwtAuthorizationFilter) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.refreshTokenService = refreshTokenService;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    @Override
    public Boolean save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).getId() != null;
    }

    @Override
    public AuthResponsePOJO signUp(AuthRequestPOJO request) {
        return getAuthResponsePOJO(request.getLogin(), request.getPassword());
    }

    @Override
    public AuthResponsePOJO register(UserDto userDto) {
        User user = findByLogin(userDto.getLogin());
        if(user != null){
            if(save(userMapper.toUser(userDto))){
                return getAuthResponsePOJO(userDto.getLogin(), user.getPassword());
            }
            throw new RuntimeException("Save user exception");
        }
        throw new UserLoginFoundException("User with login: " + userDto.getLogin() + " was found");
    }

    @Override
    public AuthResponsePOJO refresh(String refreshToken) {
        RefreshToken entity = refreshTokenService.findByRefreshToken(refreshToken);
        if(refreshToken != null){
            String accessToken = jwtAuthorizationFilter.generateAccessToken(entity.getUser().getId());
            String refreshTokenNew = refreshTokenGenerator();

            if(refreshTokenService.save(new RefreshToken(refreshTokenNew, entity.getUser()))){
                return new AuthResponsePOJO(entity.getUser().getId(), accessToken, refreshTokenNew);
            }
            throw new RuntimeException("Save RefreshToken exception");
        }
        throw new PermissionDeniedException("RefreshToken not found");
    }

    /**
     * @param login
     * @param password
     * @return AuthResponsePOJO
     * Get tokens
     */
    private AuthResponsePOJO getAuthResponsePOJO(String login, String password) {
        User user = findByLogin(login);
        if(user != null){
            if(passwordEncoder.matches(password, user.getPassword())){
                String accessToken = jwtAuthorizationFilter.generateAccessToken(user.getId());
                String refreshToken = refreshTokenGenerator();

                if(refreshTokenService.save(new RefreshToken(refreshToken, user))){
                    return new AuthResponsePOJO(user.getId(), accessToken, refreshToken);
                }
                throw new RuntimeException("Refresh token save error: " + refreshToken);
            }
            throw new UserNotFoundException("Password error: " + password);
        }
        throw new UserNotFoundException("User not found by login: " + login);
    }

    /**
     * @return String
     * RefreshToken generation (recursive)
     */
    private String refreshTokenGenerator(){
        String token = RandomStringUtils.randomAlphanumeric(128);
        if(refreshTokenService.findByRefreshToken(token) != null){
            return refreshTokenGenerator();
        } else {
            return token;
        }
    }
}
