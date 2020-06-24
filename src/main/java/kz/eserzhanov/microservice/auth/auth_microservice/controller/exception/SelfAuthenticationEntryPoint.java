package kz.eserzhanov.microservice.auth.auth_microservice.controller.exception;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SelfAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Map<String, String> map = new HashMap<>();
        map.put("errorRu", "Ошибка авторизации");
        map.put("errorKz", "Авторизация қатесі");

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(401);
        httpServletResponse.getWriter().write(new JSONObject(map).toString());
    }
}
