package kz.eserzhanov.microservice.auth.auth_microservice.config;

import kz.eserzhanov.microservice.auth.auth_microservice.config.jwt.JWTAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public long getLoggedUserId() {
        JWTAuthentication authentication = (JWTAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getUserId();
    }

}
