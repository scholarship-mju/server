package mju.scholarship.config.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.repository.MemberRepository;
import mju.scholarship.redis.Token;
import mju.scholarship.redis.TokenRepository;
import mju.scholarship.redis.TokenService;
import mju.scholarship.result.exception.JwtSignatureInvalidException;
import mju.scholarship.result.exception.MemberNotFoundException;
import mju.scholarship.result.exception.RefreshTokenNotFoundException;
import mju.scholarship.result.exception.TokenInvalidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.security.core.GrantedAuthority;


import javax.crypto.SecretKey;
import javax.lang.model.type.ErrorType;
import java.io.IOException;
import java.sql.Ref;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    @Value("${jwt.key}")
    private String key;
    private SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L; // 1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L; // 1주일

    private final TokenService tokenService;
    private static final String KEY_ROLE = "role"; // JWT의 역할(ROLE) 클레임 키

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        String accessToken = generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
        tokenService.saveAccessToken(accessToken);
        return accessToken;
    }

    public String generateRefreshToken(Authentication authentication, String accessToken) {
        return generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
    }

    private String generateToken(Authentication authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        String username = authentication.getName();
        log.info("name = {}", username);

        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        String role = optionalMember.map(member -> member.getRole().name()).orElse("ROLE_USER");


        log.info("Authentication Authorities: {}", authentication.getAuthorities());

        String jwt = Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .claim(KEY_ROLE, role) // Role을 Claim에 추가
                .expiration(expiredDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();

        log.info("jwt = {}", jwt);
        return jwt;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        // 사용자 ID (subject) 추출
        String username = claims.getSubject();

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberNotFoundException::new);

        String role = member.getRole().name();


        // 권한 정보를 고정값으로 설정 ("USER")
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        // Security의 User 객체 생성
        User principal = new User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }


    public String reissueAccessToken(String accessToken) {
        if (StringUtils.hasText(accessToken)) {
            log.info("accesToken = {}", accessToken);

            String username = extractUserIdFromAccessToken(accessToken);
            log.info("username = {}", username);
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(MemberNotFoundException::new);
            String refreshToken = member.getRefreshToken();

            // 리프레시 토큰 검증 후 괜찮은거면 액세스 토큰 재 발금
            if (validRefreshToken(refreshToken)) {
                String reissueAccessToken = generateAccessToken(getAuthentication(refreshToken));
                log.info("재발급 됨");
                return reissueAccessToken;
            }
        }
        return null;
    }

    public String extractUserIdFromAccessToken(String expiredAccessToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(expiredAccessToken)
                    .getBody();
            return claims.getSubject(); // 사용자 ID 또는 유니크 정보
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에서도 Claims를 가져올 수 있음
            return e.getClaims().getSubject();
        }
    }


    private boolean validRefreshToken(String refreshToken) {
        try {
            // JWT 파싱 및 만료 시간 확인
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey) // 서명 키 설정
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            Date expiration = claims.getExpiration(); // 만료 시간 가져오기
            return !expiration.before(new Date()); // 현재 시간보다 미래이면 유효
        } catch (Exception e) {
            // 토큰 파싱 실패 또는 만료된 경우
            return false; // 유효하지 않은 것으로 간주
        }
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return claims.getExpiration().after(new Date());
    }

    public boolean validTokenInRedis(String token) {
        return tokenService.validAccessToken(token);
    }


    public Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (MalformedJwtException e) {
            throw new TokenInvalidException();
        } catch (SecurityException e) {
            throw new JwtSignatureInvalidException();
        }
    }

}
