package mju.scholarship.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.handler.CustomExceptionHandler;
import mju.scholarship.config.provider.TokenProvider;
import mju.scholarship.result.exception.RefreshTokenNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final CustomExceptionHandler exceptionHandler;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = resolveToken(request);
            if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
                if (tokenProvider.validTokenInRedis(accessToken)) {
                    setAuthentication(accessToken);
                } else {
                    throw new IllegalArgumentException("Invalid token in Redis");
                }
            } else {
                String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);

                if (StringUtils.hasText(reissueAccessToken)) {
                    setAuthentication(reissueAccessToken);
                    response.setHeader(AUTHORIZATION, "Bearer " + reissueAccessToken);
                } else {
                    throw new IllegalStateException("Unable to reissue access token");
                }
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("필터 처리 중 예외 발생: {}", e.getMessage(), e);
            exceptionHandler.handleException(request, response, e);
        }
    }


    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7);
    }
}
