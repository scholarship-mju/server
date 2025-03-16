package mju.scholarship.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.embedding.EmbeddingService;
import mju.scholarship.member.dto.*;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.repository.MemberRepository;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final ScholarShipRepository scholarShipRepository;
    private final EmbeddingService embeddingService;

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

        loginMember.updateInfo(
                memberInfoRequest.getNickname(),
                memberInfoRequest.getPhone(),
                memberInfoRequest.getUniversity(),
                memberInfoRequest.getAge(),
                memberInfoRequest.getGender(),
                memberInfoRequest.getProvince(),
                memberInfoRequest.getCity(),
                memberInfoRequest.getDepartment(),
                memberInfoRequest.getGrade(),
                memberInfoRequest.getIncomeQuantile()
        );

        return createResponseDto(loginMember);
    }

    @Transactional
    public void firstLogin(CreateNewUserRequest createNewUserRequest) {
        Member loginMember = jwtUtil.getLoginMember();

        log.info("firstdfaat = {}", createNewUserRequest.getUniversity());
        loginMember.updateInfo(
                createNewUserRequest.getNickname(),
                createNewUserRequest.getPhone(),
                createNewUserRequest.getUniversity(),
                createNewUserRequest.getAge(),
                createNewUserRequest.getGender(),
                createNewUserRequest.getProvince(),
                createNewUserRequest.getCity(),
                createNewUserRequest.getDepartment(),
                createNewUserRequest.getGrade(),
                createNewUserRequest.getIncomeQuantile()
        );

        embeddingService.embeddingMember(loginMember.getId());

        loginMember.updateFirstLogin();
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
                .grade(member.getGrade())
                .province(member.getProvince())
                .city(member.getCity())
                .department(member.getDepartment())
                .incomeQuantile(member.getIncomeQuantile())
                .build();
    }

    @Transactional
    public RankResponse getRank() {
        PageRequest pageRequest = PageRequest.of(0, 4);

        List<Member> rank = memberRepository.getRank(pageRequest);

        RankResponse rankResponse = new RankResponse();
        rankResponse.addRanker(rank);
        return rankResponse;
    }

    @Transactional
    public void deleteAccount() {

        Member loginMember = jwtUtil.getLoginMember();

        memberRepository.delete(jwtUtil.getLoginMember());
    }
}
