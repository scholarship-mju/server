package mju.scholarship.admin;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mju.scholarship.admin.dto.MemberGotResponse;
import mju.scholarship.admin.dto.ScholarshipCrawlingRequest;
import mju.scholarship.admin.dto.ScholarshipInUnivRequest;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.MemberGot;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.member.repository.MemberGotRepository;
import mju.scholarship.member.repository.MemberRepository;
import mju.scholarship.result.ResultResponse;
import mju.scholarship.result.exception.*;
import mju.scholarship.s3.S3UploadService;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.ScholarshipService;
import mju.scholarship.scholoarship.dto.ValidAddScholarshipRequest;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
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
    private final S3UploadService s3UploadService;
    private final JwtUtil jwtUtil;

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

                if(scholarShipRepository
                        .existsByNameAndOrganizationName(record.get("상품명"), record.get("운영기관명"))){
                    continue;
                }

                Scholarship scholarship = Scholarship.builder()
                        .organizationName(record.get("운영기관명"))
                        .name(record.get("상품명"))
                        .organizationType(record.get("운영기관구분"))
                        .productType(record.get("상품구분"))
                        .financialAidType(record.get("학자금유형구분"))
                        .universityType(record.get("대학구분"))
                        .gradeType(record.get("학년구분"))
                        .departmentType(record.get("학과구분"))
                        .gradeRequirement(record.get("성적기준"))
                        .incomeRequirement(record.get("소득기준"))
                        .supportDetails(record.get("지원금액"))
                        .specialQualification(record.get("특정자격"))
                        .residencyRequirement(record.get("지역거주여부"))
                        .selectionMethod(record.get("선발방법"))
                        .selectionCount("선발인원")
                        .eligibilityRestriction(record.get("자격제한"))
                        .recommendationRequired(record.get("추천필요여부"))
                        .submitDocumentDetail(record.get("제출서류"))
                        .scholarshipUrl(record.get("홈페이지주소"))
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

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void setProgressStatus() {
        List<Scholarship> all = scholarShipRepository.findAll();
        for (Scholarship scholarship : all) {
            scholarship.updateProgressStatusConvert();
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void setProgressStatusByAsync() {
        List<Scholarship> all = scholarShipRepository.findAll();
        for (Scholarship scholarship : all) {
            asyncUpdateStatus(scholarship.getId());
        }
    }


    @Async
    @Transactional
    public void asyncUpdateStatus(Long scholarshipId) {
        Scholarship scholarship = scholarShipRepository.findById(scholarshipId).orElseThrow();
        scholarship.updateProgressStatusConvert();
    }

    public String getScholarshipImage(Long scholarshipId) {

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId).
                orElseThrow(ScholarshipNotFoundException::new);

        List<MemberGot> allScholarship = memberGotRepository.findAllByScholarship(scholarship);


        return scholarship.getScholarshipUrl();
    }

    @Transactional
    public void uploadScholarshipCrawling(ScholarshipCrawlingRequest request, MultipartFile file) throws IOException {

        String scholarshipName = request.getName();
        String organizationName = request.getOrganizationName();

        boolean alreadyExist = scholarShipRepository.existsByNameAndOrganizationName(scholarshipName, organizationName);

        if(alreadyExist) throw new AlreadyScholarshipException();

        Scholarship scholarship = Scholarship.builder()
                .organizationName(request.getOrganizationName())
                .name(request.getName())
                .organizationType(request.getOrganizationType())
                .productType(request.getProductType())
                .financialAidType(request.getFinancialAidType())
                .universityType(request.getUniversityType())
                .gradeType(request.getGradeType())
                .gradeRequirement(request.getGradeRequirement())
                .incomeRequirement(request.getIncomeRequirement())
                .supportDetails(request.getSupportDetails())
                .specialQualification(request.getSpecialQualification())
                .recommendationRequired(request.getRecommendationRequired())
                .submitDocumentDetail(request.getSubmitDocumentDetail())
                .scholarshipUrl(request.getScholarshipUrl())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .selectionMethod(request.getSelectionMethod())
                .selectionCount(request.getSelectionCount())
                .residencyRequirement(request.getResidencyRequirement())
                .eligibilityRestriction(request.getEligibilityRestriction())
                .departmentType(request.getDepartmentType())
                .build();

        scholarShipRepository.save(scholarship);

        if (file != null && !file.isEmpty()) {
            String scholarshipImage = s3UploadService.upload(file, "scholarships", scholarship.getId());
            scholarship.updateScholarshipImage(scholarshipImage);
        }

        scholarShipRepository.save(scholarship);
    }

    @Transactional
    public void uploadScholarshipInUniv(ScholarshipInUnivRequest request) {

        String scholarshipName = request.getName();
        String organizationName = request.getOrganizationName();

        boolean alreadyExist = scholarShipRepository.existsByNameAndOrganizationName(scholarshipName, organizationName);

        if(alreadyExist) throw new AlreadyScholarshipException();

        Scholarship scholarship = Scholarship.builder()
                .organizationName(request.getOrganizationName())
                .name(request.getName())
                .organizationType(request.getOrganizationType())
                .productType(request.getProductType())
                .financialAidType(request.getFinancialAidType())
                .universityType(request.getUniversityType())
                .gradeType(request.getGradeType())
                .gradeRequirement(request.getGradeRequirement())
                .incomeRequirement(request.getIncomeRequirement())
                .supportDetails(request.getSupportDetails())
                .specialQualification(request.getSpecialQualification())
                .recommendationRequired(request.getRecommendationRequired())
                .submitDocumentDetail(request.getSubmitDocumentDetail())
                .scholarshipUrl(request.getScholarshipUrl())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .selectionMethod(request.getSelectionMethod())
                .selectionCount(request.getSelectionCount())
                .residencyRequirement(request.getResidencyRequirement())
                .eligibilityRestriction(request.getEligibilityRestriction())
                .departmentType(request.getDepartmentType())
                .university(request.getUniversity())
                .build();

        scholarShipRepository.save(scholarship);
    }
}
