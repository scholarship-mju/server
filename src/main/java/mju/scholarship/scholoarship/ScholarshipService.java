package mju.scholarship.scholoarship;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.MemberGot;
import mju.scholarship.member.repository.MemberGotRepository;
import mju.scholarship.member.repository.MemberInterRepository;
import mju.scholarship.member.repository.MemberRepository;
import mju.scholarship.member.entity.MemberInterest;
import mju.scholarship.result.exception.*;
import mju.scholarship.s3.S3UploadService;
import mju.scholarship.scholoarship.dto.CreateScholarshipRequest;
import mju.scholarship.scholoarship.dto.ScholarshipResponse;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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


    @Transactional
    public void createScholarship(CreateScholarshipRequest request) {
        Scholarship scholarship = Scholarship.builder()
                .price(request.getPrice())
                .category(request.getCategory())
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

    public List<Scholarship> getAllScholarships() {
        return scholarShipRepository.findAll();
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
                .city(scholarship.getCity())
                .department(scholarship.getDepartment())
                .incomeQuantile(scholarship.getIncomeQuantile())
                .build();
    }

    public List<ScholarshipResponse> getAllGotScholarships() {

        Member loginMember = jwtUtil.getLoginMember();

        // 관심 장학금 조회 (브릿지 테이블을 통한 조회)
        List<MemberGot> memberGots = memberGotRepository.findByMember(loginMember);

        // MemberInterest -> ScholarshipResponse 변환
        return memberGots.stream()
                .map(got -> {
                    Scholarship scholarship = got.getScholarship();
                    return ScholarshipResponse.builder()
                            .id(scholarship.getId())
                            .name(scholarship.getName())
                            .minAge(scholarship.getMinAge())
                            .maxAge(scholarship.getMaxAge())
                            .university(scholarship.getUniversity())
                            .gender(scholarship.getGender())
                            .grade(scholarship.getGrade())
                            .province(scholarship.getProvince())
                            .city(scholarship.getCity())
                            .department(scholarship.getDepartment())
                            .incomeQuantile(scholarship.getIncomeQuantile())
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
                    return new ScholarshipResponse(
                            scholarship.getId(),
                            scholarship.getPrice(),
                            scholarship.getCategory(),
                            scholarship.getName(),
                            scholarship.getDescription(),
                            scholarship.getUniversity(),
                            scholarship.getMinAge(),
                            scholarship.getMaxAge(),
                            scholarship.getGender(),
                            scholarship.getProvince(),
                            scholarship.getCity(),
                            scholarship.getDepartment(),
                            scholarship.getGrade(),
                            scholarship.getIncomeQuantile()
                    );
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
    public void validGotScholarship(List<MultipartFile> files) {

        files.forEach(file -> {
            try {
                s3UploadService.upload(file, "valid");
            } catch (IOException e) {
                throw new FileUploadException();
            }
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


//    public List<Scholarship> getMyScholarship() {
        // 내 정보 가져오기

        /**
         * 내 정보랑 장학금 비교 아마 sql 짜야 될듯
         */
//    }
}
