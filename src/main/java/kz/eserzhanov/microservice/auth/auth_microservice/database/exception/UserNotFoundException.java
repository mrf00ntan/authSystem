package kz.eserzhanov.microservice.auth.auth_microservice.database.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
