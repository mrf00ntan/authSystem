package kz.eserzhanov.microservice.auth.auth_microservice.controller;

import kz.eserzhanov.microservice.auth.auth_microservice.dto.UserDto;
import kz.eserzhanov.microservice.auth.auth_microservice.pojo.AuthRequestPOJO;
import kz.eserzhanov.microservice.auth.auth_microservice.pojo.AuthResponsePOJO;
import kz.eserzhanov.microservice.auth.auth_microservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/sign-up")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody AuthResponsePOJO signUp(@RequestBody AuthRequestPOJO request){
        return userService.signUp(request);
    }

    @PostMapping(value = "/registration")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody AuthResponsePOJO register(@RequestBody UserDto request){
        return userService.register(request);
    }

    @GetMapping(value = "/refresh")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody AuthResponsePOJO refresh(@RequestParam String refreshToken){
        return userService.refresh(refreshToken);
    }
}
