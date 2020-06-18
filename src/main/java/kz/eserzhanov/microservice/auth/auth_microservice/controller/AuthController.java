package kz.eserzhanov.microservice.auth.auth_microservice.controller;

import kz.eserzhanov.microservice.auth.auth_microservice.config.jwt.JWTAuthorizationFilter;
import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.RefreshToken;
import kz.eserzhanov.microservice.auth.auth_microservice.database.exception.PermissionDeniedException;
import kz.eserzhanov.microservice.auth.auth_microservice.database.exception.UserLoginFoundException;
import kz.eserzhanov.microservice.auth.auth_microservice.database.exception.UserNotFoundException;
import kz.eserzhanov.microservice.auth.auth_microservice.dto.UserDto;
import kz.eserzhanov.microservice.auth.auth_microservice.mapper.UserMapper;
import kz.eserzhanov.microservice.auth.auth_microservice.pojo.AuthRequestPOJO;
import kz.eserzhanov.microservice.auth.auth_microservice.pojo.AuthResponsePOJO;
import kz.eserzhanov.microservice.auth.auth_microservice.service.RefreshTokenService;
import kz.eserzhanov.microservice.auth.auth_microservice.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    public AuthController(UserService userService, UserMapper userMapper, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder, JWTAuthorizationFilter jwtAuthorizationFilter) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @PostMapping(value = "/sign-up")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody AuthResponsePOJO signUp(@RequestBody AuthRequestPOJO request){
        return getAuthResponsePOJO(request.getLogin(), request.getPassword());
    }

    @PostMapping(value = "/registration")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody AuthResponsePOJO register(@RequestBody UserDto request){
        return (AuthResponsePOJO) userService.findByLogin(request.getLogin())
                .map(user -> {
                    throw new UserLoginFoundException("User with login: " + user.getLogin() + " was found");
                })
                .orElseGet(() -> {
                    if(userService.save(userMapper.toUser(request))){
                        return getAuthResponsePOJO(request.getLogin(), request.getPassword());
                    }
                    throw new RuntimeException("Internal Server Error");
                });
    }

    @GetMapping(value = "/refresh")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody AuthResponsePOJO refresh(@RequestParam String refreshToken){
        return refreshTokenService.findByRefreshToken(refreshToken)
                .map(entity -> {
                    String accessToken = jwtAuthorizationFilter.generateAccessToken(entity.getUser().getId());
                    String refreshTokenNew = refreshTokenGenerator();

                    if(refreshTokenService.save(new RefreshToken(refreshTokenNew, entity.getUser()))){
                        return new AuthResponsePOJO(accessToken, refreshTokenNew);
                    }
                    throw new RuntimeException("Internal Server Error");
                })
                .orElseGet(() -> { throw new PermissionDeniedException("RefreshToken not found"); });
    }

    /**
     * @param login
     * @param password
     * @return AuthResponsePOJO
     * get tokens
     */
    private AuthResponsePOJO getAuthResponsePOJO(String login, String password) {
        return userService.findByLogin(login)
                .map(user -> {
                    if(passwordEncoder.matches(password, user.getPassword())){
                        String accessToken = jwtAuthorizationFilter.generateAccessToken(user.getId());
                        String refreshToken = refreshTokenGenerator();

                        if(refreshTokenService.save(new RefreshToken(refreshToken, user))){
                            return new AuthResponsePOJO(accessToken, refreshToken);
                        }
                        throw new RuntimeException("Internal Server Error");
                    }
                    throw new UserNotFoundException("Password error");
                })
                .orElseGet(() -> { throw new UserNotFoundException("User not found by login: " + login); });
    }

    /**
     * @return String
     * RefreshToken generation
     */
    private String refreshTokenGenerator(){
        String token = RandomStringUtils.randomAlphanumeric(128);
        if(refreshTokenService.findByRefreshToken(token).isPresent()){
            return refreshTokenGenerator();
        } else {
            return token;
        }
    }
}
