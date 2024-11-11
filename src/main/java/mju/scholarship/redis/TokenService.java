package mju.scholarship.redis;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.result.exception.TokenExpiredException;
import mju.scholarship.result.exception.TokenNotFoundException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final TokenRepository tokenRepository;
    private final StringRedisTemplate redisTemplate;

    public void deleteRefreshToken(String authToken) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(authToken))) {
            redisTemplate.delete(authToken);
            tokenRepository.deleteById(authToken);
            log.info("Redis에서 토큰이 성공적으로 삭제되었습니다.");
        } else {
            log.warn("삭제하려는 토큰이 Redis에 존재하지 않습니다.");
        }
    }

    @Transactional
    public boolean validTokenInRedis(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken).isPresent();
    }

    @Transactional
    public void saveOrUpdate(String memberKey, String refreshToken, String accessToken) {
        Token token = tokenRepository.findByAccessToken(accessToken)
                .map(o -> o.updateRefreshToken(refreshToken))
                .orElseGet(() -> new Token(memberKey, refreshToken, accessToken));

        tokenRepository.save(token);
    }

    public Token findByAccessTokenOrThrow(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(TokenNotFoundException::new);
    }

    @Transactional
    public void updateToken(String accessToken, Token token) {
        token.updateAccessToken(accessToken);
        tokenRepository.save(token);
    }


}
