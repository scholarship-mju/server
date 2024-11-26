package mju.scholarship.member.dto;

import lombok.Builder;
import lombok.Getter;
import mju.scholarship.member.entity.Member;
import mju.scholarship.result.exception.IllegalRegistrationIdException;

import java.util.Map;

@Builder
@Getter
public class OAuth2UserInfo {

    private String name;
    private String email;
    private String profile;

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            // AuthException(ILLEGAL_REGISTRATION_ID) 으로 바꿔야 함.
            default -> throw new IllegalRegistrationIdException();

        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("picture"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .build();
    }

    public Member toMember() {
        return Member.builder()
                .email(email)
                .username(name)
                .build();
    }
}
