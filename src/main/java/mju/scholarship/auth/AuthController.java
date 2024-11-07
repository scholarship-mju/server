package mju.scholarship.auth;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.provider.TokenProvider;
import mju.scholarship.redis.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String tokenHeader) {
        String accessToken = tokenHeader.replace("Bearer ", "");

        // 사용자 ID를 액세스 토큰으로 추출 (구현 필요)
        String userId = getUserIdFromToken(accessToken);
        log.info("Logout success with token: {}", accessToken);

        // 로그아웃 처리
        authService.logout(accessToken);

        return ResponseEntity.ok("로그아웃 성공");
    }

    private String getUserIdFromToken(String token) {
        // 액세스 토큰에서 사용자 ID 추출 로직 구현
        Claims claims = tokenProvider.parseClaims(token);
        return claims.getSubject();
    }
}
