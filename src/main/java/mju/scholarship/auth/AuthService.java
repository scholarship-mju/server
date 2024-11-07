package mju.scholarship.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.auth.dto.KakaoTokenResponse;
import mju.scholarship.auth.dto.KakaoUserResponse;
import mju.scholarship.member.Member;
import mju.scholarship.member.MemberRepository;
import mju.scholarship.redis.Token;
import mju.scholarship.redis.TokenRepository;
import mju.scholarship.result.exception.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final RestTemplate restTemplate;
    private final TokenRepository tokenRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void logout(String accessToken) {
        Token token = tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(TokenExpiredException::new);

        tokenRepository.delete(token);
    }

}
