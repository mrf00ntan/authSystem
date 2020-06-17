package kz.eserzhanov.microservice.auth.auth_microservice.database.exception;

public class UserLoginFoundException extends RuntimeException {
    public UserLoginFoundException(String message) {
        super(message);
    }
}
