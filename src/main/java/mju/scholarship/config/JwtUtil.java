package mju.scholarship.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.repository.MemberRepository;
import mju.scholarship.result.exception.MemberNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
public class JwtUtil {

    private final MemberRepository memberRepository;

    public Member getLoginMember(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("name = {}", name);

        return memberRepository.findByUsername(name)
                .orElseThrow(MemberNotFoundException::new);
    }


}
