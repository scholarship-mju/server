package mju.scholarship.scholoarship;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.MemberGot;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.member.repository.MemberGotRepository;
import mju.scholarship.member.repository.MemberInterRepository;
import mju.scholarship.member.repository.MemberRepository;
import mju.scholarship.member.entity.MemberInterest;
import mju.scholarship.result.exception.*;
import mju.scholarship.s3.S3UploadService;
import mju.scholarship.scholoarship.dto.*;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScholarshipService {

    private final ScholarShipRepository scholarShipRepository;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final MemberInterRepository memberInterRepository;
    private final MemberGotRepository memberGotRepository;
    private final S3UploadService s3UploadService;

    // 매일 자정 실행
    @Scheduled(cron = "0 0 0 * * ?") // cron 표현식: 매일 00:00:00
    @Transactional
    public void updateProgressStatus() {
        List<Scholarship> scholarships = scholarShipRepository.findAll();

        for (Scholarship scholarship : scholarships) {
            scholarship.updateProgressStatus(); // 상태 업데이트
        }

        scholarShipRepository.saveAll(scholarships); // 일괄 저장
        System.out.println("Scholarship progressStatus updated at: " + LocalDate.now());
    }


    @Transactional
    public void createScholarship(CreateScholarshipRequest request) {
        Scholarship scholarship = Scholarship.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .detailEligibility(request.getDetailEligibility())
                .price(request.getPrice())
                .name(request.getName())
                .minAge(request.getMinAge())
                .maxAge(request.getMaxAge())
                .university(request.getUniversity())
                .gender(request.getGender())
                .grade(request.getGrade())
                .province(request.getProvince())
                .city(request.getCity())
                .department(request.getDepartment())
                .incomeQuantile(request.getIncomeQuantile())
                .build();

        scholarShipRepository.save(scholarship);
    }

    public List<AllScholarshipResponse> getAllScholarships(ScholarshipFilterRequest filterRequest) {
        // 현재 로그인된 사용자 가져오기
        Member loginMember = jwtUtil.getLoginMember();

        // 관심 장학금 ID 리스트 가져오기
        List<Long> interestedIds = memberInterRepository.findScholarshipIdByMember(loginMember);

        // 전체 장학금 조회 및 관심 여부 설정
        return scholarShipRepository.findAllByFilter(filterRequest).stream()
                .map(scholarship -> AllScholarshipResponse.builder()
                        .id(scholarship.getId())
                        .price(scholarship.getPrice())
                        .name(scholarship.getName())
                        .isInterested(interestedIds.contains(scholarship.getId())) // 관심 여부 체크
                        .progressStatus(scholarship.getProgressStatus())
                        .build()
                )
                .collect(Collectors.toList());
    }


    @Transactional
    public void addGotScholarships(Long scholarshipId) {
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        boolean exists = memberGotRepository.existsByMemberAndScholarship(loginMember, scholarship);
        if (exists) {
            throw new AlreadyGotScholarshipException(); // 커스텀 예외
        }

        MemberGot memberGot = MemberGot.builder()
                .member(loginMember)
                .scholarship(scholarship)
                .build();

        memberGotRepository.save(memberGot);
    }

    @Transactional
    public void addInterestScholarship(Long scholarshipId) {
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        boolean exists = memberInterRepository.existsByMemberAndScholarship(loginMember, scholarship);
        if (exists) {
            throw new AlreadyInterestedScholarshipException(); // 커스텀 예외
        }

        MemberInterest memberInterest = MemberInterest.builder()
                .member(loginMember)
                .scholarship(scholarship)
                .build();

        memberInterRepository.save(memberInterest);
    }

    public ScholarshipResponse getOneScholarship(Long scholarshipId) {
        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        return ScholarshipResponse.builder()
                .id(scholarship.getId())
                .name(scholarship.getName())
                .minAge(scholarship.getMinAge())
                .maxAge(scholarship.getMaxAge())
                .university(scholarship.getUniversity())
                .gender(scholarship.getGender())
                .grade(scholarship.getGrade())
                .province(scholarship.getProvince())
                .price(scholarship.getPrice())
                .city(scholarship.getCity())
                .progressStatus(scholarship.getProgressStatus())
                .submission(scholarship.getSubmission())
                .startDate(scholarship.getStartDate())
                .endDate(scholarship.getEndDate())
                .detailEligibility(scholarship.getDetailEligibility())
                .department(scholarship.getDepartment())
                .incomeQuantile(scholarship.getIncomeQuantile())
                .build();
    }

    public List<GotScholarshipResponse> getAllGotScholarships() {
        Member loginMember = jwtUtil.getLoginMember();

        // 브릿지 테이블(MemberGot)에서 로그인한 회원의 모든 정보 조회
        List<MemberGot> memberGots = memberGotRepository.findByMember(loginMember);

        // MemberGot -> GotScholarshipResponse 변환
        return memberGots.stream()
                .map(got -> {
                    Scholarship scholarship = got.getScholarship();
                    return GotScholarshipResponse.builder()
                            .id(scholarship.getId())
                            .name(scholarship.getName())
                            .price(scholarship.getPrice())
                            .status(got.getStatus())
                            .build();

                })
                .collect(Collectors.toList());
    }


    public List<ScholarshipResponse> getAllInterestScholarships() {

        Member loginMember = jwtUtil.getLoginMember();

        // 관심 장학금 조회 (브릿지 테이블을 통한 조회)
        List<MemberInterest> interests = memberInterRepository.findByMember(loginMember);

        // MemberInterest -> ScholarshipResponse 변환
        return interests.stream()
                .map(interest -> {
                    Scholarship scholarship = interest.getScholarship();
                    return ScholarshipResponse.builder()
                            .id(scholarship.getId())
                            .name(scholarship.getName())
                            .price(scholarship.getPrice())
                            .minAge(scholarship.getMinAge())
                            .maxAge(scholarship.getMaxAge())
                            .university(scholarship.getUniversity())
                            .gender(scholarship.getGender())
                            .grade(scholarship.getGrade())
                            .province(scholarship.getProvince())
                            .city(scholarship.getCity())
                            .department(scholarship.getDepartment())
                            .incomeQuantile(scholarship.getIncomeQuantile())
                            .detailEligibility(scholarship.getDetailEligibility())
                            .startDate(scholarship.getStartDate())
                            .endDate(scholarship.getEndDate())
                            .submission(scholarship.getSubmission())
                            .progressStatus(scholarship.getProgressStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteInterestScholarship(Long scholarshipId) {
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        MemberInterest memberInterest = memberInterRepository.findByMemberAndScholarship(loginMember, scholarship)
                .orElseThrow(InterestedScholarshipNotFoundException::new);

        memberInterRepository.delete(memberInterest);
    }

    //Todo : 장학금 삭제하면 받은 장학금이나 찜한 장학금 목록에서도 다 삭제
    @Transactional
    public void deleteScholarship(Long scholarshipId) {
        log.info("id = {}", scholarshipId);
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);
    }

    public List<Scholarship> getMyScholarship() {
        Member loginMember = jwtUtil.getLoginMember();

        List<Scholarship> myScholarships = scholarShipRepository.findMyScholarship(loginMember);

        return myScholarships;
    }

    @Transactional
    public void validGotScholarship(Long scholarshipId, List<MultipartFile> files) {

        Member loginMember = jwtUtil.getLoginMember();

        String sid = String.valueOf(scholarshipId);

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        MemberGot memberGot = memberGotRepository.findByMemberAndScholarship(loginMember, scholarship)
                .orElseThrow(MemberNotFoundException::new);

        files.forEach(file -> {
            try {
                s3UploadService.upload(file, "valid", loginMember.getId(), scholarshipId);
            } catch (IOException e) {
                throw new FileUploadException();
            }
            memberGot.changeStatus(ScholarshipStatus.IN_PROGRESS);
        });
    }

    @Transactional
    public void deleteGotScholarship(Long scholarshipId) {
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        MemberGot memberGot = memberGotRepository.findByMemberAndScholarship(loginMember, scholarship)
                .orElseThrow(InterestedScholarshipNotFoundException::new);

        memberGotRepository.delete(memberGot);
    }

    @Transactional
    public void validAddGotScholarship(ValidAddScholarshipRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Scholarship scholarship = scholarShipRepository.findById(request.getScholarshipId())
                .orElseThrow(ScholarshipNotFoundException::new);

        MemberGot memberGot = memberGotRepository.findByMemberAndScholarship(member, scholarship)
                .orElseThrow(GotScholarshipNotFoundException::new);

        memberGot.changeStatus(ScholarshipStatus.VERIFIED);

        member.addTotal(Integer.parseInt(scholarship.getPrice())); // 변환된 int 값을 전달

    }


//    public List<Scholarship> getMyScholarship() {
        // 내 정보 가져오기

        /**
         * 내 정보랑 장학금 비교 아마 sql 짜야 될듯
         */
//    }
}
