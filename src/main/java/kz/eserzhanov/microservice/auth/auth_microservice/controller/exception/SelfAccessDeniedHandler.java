package kz.eserzhanov.microservice.auth.auth_microservice.controller.exception;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SelfAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("errorRu", "Отказано в доступе");
        map.put("errorKz", "Қол жеткізуден бас тартылды");

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(403);
        httpServletResponse.getWriter().write(new JSONObject(map).toString());
    }
}
