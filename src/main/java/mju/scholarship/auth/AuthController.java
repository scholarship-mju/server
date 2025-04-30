package mju.scholarship.auth;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.provider.TokenProvider;
import mju.scholarship.member.PrincipalDetails;
import mju.scholarship.redis.TokenService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String tokenHeader) {
        String accessToken = tokenHeader.replace("Bearer ", "");

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
