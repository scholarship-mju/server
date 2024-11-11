package mju.scholarship.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mju.scholarship.member.dto.OAuth2UserInfo;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> userAttribute = super.loadUser(userRequest).getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, userAttribute);

        AtomicBoolean isFirstLogin = new AtomicBoolean(false);

        // 이메일 중복 체크
        Member member = memberRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElseGet(() -> {
                    // 유저네임 중복 체크 추가
                    if (memberRepository.existsByUsername(oAuth2UserInfo.getName())) {
                        throw new RuntimeException("이미 존재하는 사용자 이름입니다.");
                    }
                    if (memberRepository.existsByEmail(oAuth2UserInfo.getEmail())) {
                        throw new RuntimeException("이미 존재하는 이메일입니다.");
                    }

                    isFirstLogin.set(true); // 처음 로그인 플래그 설정
                    return memberRepository.save(oAuth2UserInfo.toMember());
                });

        return new PrincipalDetails(member, userAttribute, userNameAttributeName, isFirstLogin.get());
    }


}
