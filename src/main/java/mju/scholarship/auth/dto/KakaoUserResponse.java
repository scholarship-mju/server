package mju.scholarship.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class KakaoUserResponse {
    private Long id;
    private KakaoAccount kakaoAccount;

    @Getter
    @Setter
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        @Setter
        public static class Profile {
            private String nickname;
        }
    }
}

