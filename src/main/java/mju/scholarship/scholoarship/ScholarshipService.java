package mju.scholarship.scholoarship;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.Member;
import mju.scholarship.member.MemberRepository;
import mju.scholarship.result.exception.ScholarshipNotFoundException;
import mju.scholarship.scholoarship.dto.CreateScholarshipRequest;
import mju.scholarship.scholoarship.dto.ScholarshipResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScholarshipService {

    private final ScholarShipRepository scholarShipRepository;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    /**
     * 장학금 생성 메소드
     * @param request
     */
    @Transactional
    public void createScholarship(CreateScholarshipRequest request) {
        Scholarship scholarship = Scholarship.builder()
                .name(request.getName())
                .age(request.getAge())
                .university(request.getUniversity())
                .gender(request.getGender())
                .grade(request.getGrade())
                .city(request.getCity())
                .department(request.getDepartment())
                .incomeQuantile(request.getIncomeQuantile())
                .build();

        scholarShipRepository.save(scholarship);
    }

    /**
     * 전체 장학금 조회 메소드
     * @return
     */
    public List<Scholarship> getAllScholarships() {
        return scholarShipRepository.findAll();
    }

    /**
     * 내가 이미 받은 장학금 추가(등록)
     * @param name 장학금 이름
     * @return
     */
    @Transactional
    public void registerGotScholarships(String name) {
        Scholarship scholarship = scholarShipRepository.findByName(name)
                .orElseThrow(ScholarshipNotFoundException::new);

        Member loginMember = jwtUtil.getLoginMember();
        loginMember.addGotScholarship(scholarship);
        memberRepository.save(loginMember);

    }


//    public List<Scholarship> getMyScholarship() {
        // 내 정보 가져오기

        /**
         * 내 정보랑 장학금 비교 아마 sql 짜야 될듯
         */
//    }
}
