package mju.scholarship.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.provider.TokenProvider;
import mju.scholarship.member.MemberRepository;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // accessToken, refreshToken 발급
        String accessToken = tokenProvider.generateAccessToken(authentication);
        tokenProvider.generateRefreshToken(authentication, accessToken);


        // 토큰 전달을 위한 redirect
        String redirectUrl = UriComponentsBuilder.fromUriString("http://ec2-15-164-84-210.ap-northeast-2.compute.amazonaws.com:3000/auth/success")
                .queryParam("accessToken", accessToken)
                .build().toUriString();
        log.info("redirect to {}", redirectUrl);

        response.sendRedirect(redirectUrl);
    }

//    public boolean checkNewUser(Authentication authentication){
//        return memberRepository.findByEmail(authentication.)
//    }

}
