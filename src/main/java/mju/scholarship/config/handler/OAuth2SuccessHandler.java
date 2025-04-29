package mju.scholarship.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.config.provider.TokenProvider;
import mju.scholarship.member.PrincipalDetails;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private static final String URI = "/auth/success";
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // accessToken 발급
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication, accessToken);
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        boolean isFirstLogin = principal.isFirstLogin();

        Member loginMember = jwtUtil.getLoginMember();
        loginMember.updateRefreshToken(refreshToken);
        memberRepository.save(loginMember);
        log.info("로그인 성공해서 받은 액세스 토큰 = {}", accessToken);




        // 토큰과 첫 로그인 여부를 포함하여 리다이렉트 URL 생성
        String redirectUrl = UriComponentsBuilder.fromUriString("https://finding-scholarship.vercel.app/auth/success")
                .queryParam("accessToken", accessToken)
                .queryParam("isFirstLogin", isFirstLogin) // 첫 로그인 여부 추가
                .build().toUriString();

        log.info("redirect to {}", redirectUrl);

        response.sendRedirect(redirectUrl);
    }

    // 첫 로그인 여부 확인 메서드

}
