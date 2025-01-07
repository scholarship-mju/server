package mju.scholarship.config.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomExceptionHandler {

    public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        // 로그 작성
        String errorMessage = e.getMessage();
        String requestURI = request.getRequestURI();

        // 클라이언트 응답 작성
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"code\": \"T501\", \"message\": \"" + errorMessage + "\"}");
        response.getWriter().flush();
    }
}
