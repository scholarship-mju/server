package mju.scholarship.member;

import lombok.Getter;
import mju.scholarship.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class PrincipalDetails implements OAuth2User, UserDetails {

    private Member member;
    private Map<String, Object> attributes;
    private String attributeKey;
    private boolean isFirstLogin;

    public PrincipalDetails(Member member, Map<String, Object> attributes, String attributeKey, boolean isFirstLogin) {
        this.member = member;
        this.attributes = attributes;
        this.attributeKey = attributeKey;
        this.isFirstLogin = isFirstLogin;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }


    @Override
    public String getPassword() {
        return "";
    }


    //리턴 해주는거 바꿔야함
    @Override
    public String getUsername() {
        return member.getUsername();
    }

    public String getEmail(){ return member.getEmail(); }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return "";
    }
}
