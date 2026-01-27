package jaram.jaramplus.mopp_service.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        String requestUri = request.getRequestURI();

        // /admin/** 경로는 /admin/login으로 리다이렉트
        if (requestUri.startsWith("/admin")) {
            response.sendRedirect("/admin/login");
            return;
        }

        // 그 외 API 요청은 401 응답
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Authentication required\"}");
    }
}
