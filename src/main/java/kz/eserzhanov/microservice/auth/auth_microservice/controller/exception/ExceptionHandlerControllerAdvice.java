package kz.eserzhanov.microservice.auth.auth_microservice.controller.exception;

import kz.eserzhanov.microservice.auth.auth_microservice.database.exception.PermissionDeniedException;
import kz.eserzhanov.microservice.auth.auth_microservice.database.exception.UserLoginFoundException;
import kz.eserzhanov.microservice.auth.auth_microservice.database.exception.UserNotFoundException;
import kz.eserzhanov.microservice.auth.auth_microservice.database.exception.WrongPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler implements AccessDeniedHandler {
    @ExceptionHandler(value = { PermissionDeniedException.class })
    protected ResponseEntity<Object> permissionDeniedHandle(PermissionDeniedException ex, WebRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("errorRu", "Отказано в доступе");
        map.put("errorKz", "Қол жеткізуден бас тартылды");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
    }

    @ExceptionHandler(value = { UserLoginFoundException.class })
    protected ResponseEntity<Object> userLoginFoundExceptionHandle(UserLoginFoundException ex, WebRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("errorRu", "Пользователь с таким логинов не найден");
        map.put("errorKz", "Мұндай логині бар пайдаланушы табылмады");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(value = { UserNotFoundException.class })
    protected ResponseEntity<Object> userNotFoundExceptionHandle(UserNotFoundException ex, WebRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("errorRu", "Пользователь не найден");
        map.put("errorKz", "Пайдаланушы табылмады");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(value = { WrongPasswordException.class })
    protected ResponseEntity<Object> wrongPasswordExceptionHandle(WrongPasswordException ex, WebRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("errorRu", "Неправильный пароль");
        map.put("errorKz", "Қате пароль");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(value = { RuntimeException.class })
    protected ResponseEntity<Object> runtimeExceptionHandle(RuntimeException ex, WebRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("errorRu", "Ошибка сервера");
        map.put("errorKz", "Сервер қатесі");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/exception/permission-denied");
    }
}
