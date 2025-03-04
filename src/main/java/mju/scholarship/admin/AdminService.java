package mju.scholarship.admin;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mju.scholarship.admin.dto.MemberGotResponse;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.MemberGot;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.member.repository.MemberGotRepository;
import mju.scholarship.member.repository.MemberRepository;
import mju.scholarship.result.exception.CsvParsingException;
import mju.scholarship.result.exception.GotScholarshipNotFoundException;
import mju.scholarship.result.exception.MemberNotFoundException;
import mju.scholarship.result.exception.ScholarshipNotFoundException;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.ScholarshipService;
import mju.scholarship.scholoarship.dto.ValidAddScholarshipRequest;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberGotRepository memberGotRepository;
    private final MemberRepository memberRepository;
    private final ScholarShipRepository scholarShipRepository;

    public List<MemberGotResponse> gotScholarshipConfirm(ScholarshipStatus status) {

        List<MemberGot> allByStatus = memberGotRepository.findAllByStatus(status);
        List<MemberGotResponse> allByStatusResponse = new ArrayList<>();

        for(MemberGot memberGot : allByStatus){
            MemberGotResponse memberGotResponse = MemberGotResponse.builder()
                    .memberGotId(memberGot.getId())
                    .scholarshipName(memberGot.getScholarship().getName())
                    .scholarshipId(memberGot.getScholarship().getId())
                    .memberName(memberGot.getMember().getNickname())
                    .memberId(memberGot.getMember().getId())
                    .status(memberGot.getStatus())
                    .imageUrl(memberGot.getImageUrl())
                    .build();

            allByStatusResponse.add(memberGotResponse);
        }

        return allByStatusResponse;
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

        member.addTotal();
    }

    @Transactional
    public void uploadScholarshipCsv(MultipartFile file) {
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            List<Scholarship> scholarships = new ArrayList<>();

            for (String header : csvParser.getHeaderMap().keySet()) {
                System.out.println("Found Header: " + header);
            }

            for (CSVRecord record : csvParser) {
                Scholarship scholarship = Scholarship.builder()
                        .organizationName(record.get("운영기관명"))
                        .name(record.get("상품명"))
                        .organizationType(record.get("운영기관구분"))
                        .productType(record.get("상품구분"))
                        .financialAidType(record.get("학자금유형구분"))
                        .universityType(record.get("대학구분"))
                        .gradeType(record.get("학년구분"))
                        .departmentType(record.get("학과구분"))
                        .gradeRequirement(record.get("성적기준 상세내용"))
                        .incomeRequirement(record.get("소득기준 상세내용"))
                        .supportDetails(record.get("지원내역 상세내용"))
                        .specialQualification(record.get("특정자격 상세내용"))
                        .residencyRequirement(record.get("지역거주여부 상세내용"))
                        .selectionMethod(record.get("선발방법 상세내용"))
                        .selectionCount(StringUtils.isNumeric(record.get("선발인원 상세내용")) ? Integer.parseInt(record.get("선발인원 상세내용")) : 0)
                        .eligibilityRestriction(record.get("자격제한 상세내용"))
                        .recommendationRequired("TRUE".equalsIgnoreCase(record.get("추천필요여부 상세내용")) || "1".equals(record.get("추천필요여부 상세내용")))
                        .submitDocumentDetail(record.get("제출서류 상세내용"))
                        .scholarshipUrl(record.get("홈페이지 주소"))
                        .startDate(record.get("모집시작일"))
                        .endDate(record.get("모집종료일"))
                        .build();

                scholarships.add(scholarship);
            }

            scholarShipRepository.saveAll(scholarships);
            csvParser.close();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
