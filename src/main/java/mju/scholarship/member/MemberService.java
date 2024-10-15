package mju.scholarship.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.dto.LoginDto;
import mju.scholarship.member.dto.MemberInfoRequest;
import mju.scholarship.member.dto.SignupDto;
import mju.scholarship.member.dto.UpdateMemberInfoRequest;
import mju.scholarship.result.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public void login(LoginDto loginDto) {
        // 로그인 로직
        Member member = memberRepository.findByPassword(loginDto.getPassword())
                .orElseThrow(MemberNotFoundException::new);

    }

    public void signup(SignupDto signupDto) {
        //회원가입 로직

    }

    @Transactional
    public void createInfo(MemberInfoRequest memberInfoRequest) {
        //내 정보 입력 로직
        Member loginMember = jwtUtil.getLoginMember();

        loginMember.createInfo(
                memberInfoRequest.getUniversity(),
                memberInfoRequest.getAge(),
                memberInfoRequest.getGender(),
                memberInfoRequest.getCity(),
                memberInfoRequest.getDepartment(),
                memberInfoRequest.getGrade(),
                memberInfoRequest.getIncomeQuantile()
        );

        memberRepository.save(loginMember);

    }

    @Transactional
    public void updateInfo(UpdateMemberInfoRequest request) {
        Member loginMember = jwtUtil.getLoginMember();

        loginMember.updateInfo(
                request.getEmail(),
                request.getPhone(),
                request.getPassword(),
                request.getUniversity(),
                request.getAge(),
                request.getGender(),
                request.getCity(),
                request.getDepartment(),
                request.getGrade(),
                request.getIncomeQuantile()
        );

        memberRepository.save(loginMember);
    }
}
