package mju.scholarship.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.member.repository.MemberRepository;
import mju.scholarship.redis.Token;
import mju.scholarship.redis.TokenRepository;
import mju.scholarship.redis.TokenService;
import mju.scholarship.result.exception.TokenExpiredException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final RestTemplate restTemplate;
    private final TokenService tokenService;

    @Transactional
    public void logout(String accessToken) {

        tokenService.blacklistAccessToken(accessToken);
    }

}
