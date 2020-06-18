package kz.eserzhanov.microservice.auth.auth_microservice.controller.exception;

import kz.eserzhanov.microservice.auth.auth_microservice.database.exception.PermissionDeniedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {
    @GetMapping(value = "/permission-denied")
    public ResponseEntity<Map<String, String>> permissionDenied(){
        throw new PermissionDeniedException("Unable to access the protected URL");
    }
}
