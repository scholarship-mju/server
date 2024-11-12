package mju.scholarship.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.dto.*;
import mju.scholarship.result.exception.MemberNotFoundException;
import mju.scholarship.result.exception.ScholarshipNotFoundException;
import mju.scholarship.scholoarship.ScholarShipRepository;
import mju.scholarship.scholoarship.Scholarship;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final ScholarShipRepository scholarShipRepository;

    @Transactional
    public void createInfo(MemberInfoRequest memberInfoRequest) {
        //내 정보 입력 로직
        Member loginMember = jwtUtil.getLoginMember();

        loginMember.createInfo(
                memberInfoRequest.getNickname(),
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
    public MemberResponse updateInfo(UpdateMemberInfoRequest memberInfoRequest) {
        Member loginMember = jwtUtil.getLoginMember();

        updateInfo(memberInfoRequest, loginMember);

        return createResponseDto(loginMember);
    }

    @Transactional
    public void firstLogin(UpdateMemberInfoRequest firstLoginRequest) {
        Member loginMember = jwtUtil.getLoginMember();
        log.info("loginMember = {}", loginMember);

        loginMember.updateInfo(
                firstLoginRequest.getNickname(),
                firstLoginRequest.getPhone(),
                firstLoginRequest.getUniversity(),
                firstLoginRequest.getAge(),
                firstLoginRequest.getGender(),
                firstLoginRequest.getCity(),
                firstLoginRequest.getDepartment(),
                firstLoginRequest.getGrade(),
                firstLoginRequest.getIncomeQuantile()
        );
        memberRepository.save(loginMember);
    }

    public MemberResponse getMyInfo() {
        Member loginMember = jwtUtil.getLoginMember();

        return createResponseDto(loginMember);
    }

    private static MemberResponse createResponseDto(Member member) {
        return MemberResponse.builder()
                .nickname(member.getNickname())
                .username(member.getUsername())
                .email(member.getEmail())
                .phone(member.getPhone())
                .university(member.getUniversity())
                .age(member.getAge())
                .gender(member.getGender())
                .city(member.getCity())
                .department(member.getDepartment())
                .incomeQuantile(member.getIncomeQuantile())
                .build();
    }

    @Transactional
    private static void updateInfo(UpdateMemberInfoRequest memberInfoRequest, Member loginMember) {

    }
}
