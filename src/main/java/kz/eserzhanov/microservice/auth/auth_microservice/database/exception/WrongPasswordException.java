package kz.eserzhanov.microservice.auth.auth_microservice.database.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Wrong password");
    }
}
