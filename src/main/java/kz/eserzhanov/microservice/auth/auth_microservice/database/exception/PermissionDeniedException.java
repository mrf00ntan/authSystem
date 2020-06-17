package kz.eserzhanov.microservice.auth.auth_microservice.database.exception;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}
