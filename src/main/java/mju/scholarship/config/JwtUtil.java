package mju.scholarship.config;

import lombok.RequiredArgsConstructor;
import mju.scholarship.member.Member;
import mju.scholarship.member.MemberRepository;
import mju.scholarship.result.exception.MemberNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final MemberRepository memberRepository;

    public Member getLoginMember(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        return memberRepository.findByUsername(name)
                .orElseThrow(MemberNotFoundException::new);
    }


}
