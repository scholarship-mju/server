package mju.scholarship.config.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.Instant;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
//        log.error("AccessDeniedException is occurred. ", accessDeniedException);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"code\": \"ACCESS_DENIED\"," + "\n"
                        + "\"status\": \"403\"," + "\n"
                        + "\"message\": \"접근 권한이 없습니다.\"," + "\n"
                        + "\"occurredAt\": \"" + Instant.now() + "\"}");
    }
}
