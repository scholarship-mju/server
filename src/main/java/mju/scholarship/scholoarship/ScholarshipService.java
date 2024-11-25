package mju.scholarship.scholoarship;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.Member;
import mju.scholarship.member.MemberRepository;
import mju.scholarship.result.exception.FileUploadException;
import mju.scholarship.result.exception.ScholarshipNotFoundException;
import mju.scholarship.s3.S3UploadService;
import mju.scholarship.scholoarship.dto.CreateScholarshipRequest;
import mju.scholarship.scholoarship.dto.ScholarshipResponse;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.springframework.beans.factory.annotation.Value;
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

    /**
     * 전체 장학금 조회 메소드
     * TODO : Paging
     * @return
     */
    public List<Scholarship> getAllScholarships() {
        return scholarShipRepository.findAll();
    }

    /**
     * 내가 이미 받은 장학금 추가(등록)
     * @param
     * @return
     */
    @Transactional
    public void addGotScholarships(Long scholarshipId) {
        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        Member loginMember = jwtUtil.getLoginMember();
        loginMember.addGotScholarship(scholarship);
        loginMember.addTotal(scholarship.getPrice());
        memberRepository.save(loginMember);

    }

    @Transactional
    public void addInterestScholarship(Long scholarshipId) {
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        loginMember.addInterestScholarship(scholarship);
        memberRepository.save(loginMember);
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

        List<Scholarship> gotScholarships = loginMember.getGotScholarships();

        //Scholarship -> ScholarshipResponse 로 변환해서 리턴
        return gotScholarships.stream()
                .map(scholarship -> new ScholarshipResponse(
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
                        scholarship.getIncomeQuantile()))
                .collect(Collectors.toList());
    }

    public List<ScholarshipResponse> getAllInterestScholarships() {

        Member loginMember = jwtUtil.getLoginMember();

        List<Scholarship> interestScholarships = loginMember.getInterestScholarships();

        //Scholarship -> ScholarshipResponse 로 변환해서 리턴
        return interestScholarships.stream()
                .map(scholarship -> new ScholarshipResponse(
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
                        scholarship.getIncomeQuantile()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteInterestScholarship(Long scholarshipId) {

        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        loginMember.deleteInterestScholarship(scholarship);

        memberRepository.save(loginMember);
    }

    @Transactional
    public void deleteScholarship(Long scholarshipId) {
        log.info("id = {}", scholarshipId);
        Member loginMember = jwtUtil.getLoginMember();
        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);
        loginMember.deleteGotScholarship(scholarship);
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


//    public List<Scholarship> getMyScholarship() {
        // 내 정보 가져오기

        /**
         * 내 정보랑 장학금 비교 아마 sql 짜야 될듯
         */
//    }
}
