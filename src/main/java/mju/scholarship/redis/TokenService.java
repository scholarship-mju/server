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

    public void saveAccessToken(String accessToken) {
        Token token = new Token(accessToken); // Token 객체 생성
        tokenRepository.save(token); // Redis에 저장
    }

    public void deleteAccessToken(String id) {
        tokenRepository.deleteById(id); // Redis에서 ID로 삭제
    }

    public boolean validAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken).isPresent();
    }


}
