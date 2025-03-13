package mju.scholarship.scholoarship;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Scholarship {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scholarship_id")
    private Long id;

    /**
     * 장학금 조건
     */
    private String organizationName; // 운영기관명
    private String name; // 장학금 이름
    private String organizationType;
    private String productType; // 상품구분
    private String financialAidType; // 학자금유형구분
    private String universityType; // 대학구분
    private String gradeType; // 학년구분
    private String departmentType; // 학과구분
    @Column(columnDefinition = "TEXT")
    private String gradeRequirement; // 성적기준
    private String incomeRequirement; // 소득기준
    @Column(columnDefinition = "TEXT")
    private String supportDetails; // 지원내역 상세 내용

    private String specialQualification; // 특정자격
    private String residencyRequirement; // 지역거주여부
    private String selectionMethod; // 선발방법
    private Integer selectionCount; // 선발인원
    private String eligibilityRestriction; // 자격제한
    private Boolean recommendationRequired; // 추천필요여부
    @Column(columnDefinition = "TEXT")
    private String  submitDocumentDetail; //제출 서류 상세 내용
    private String scholarshipUrl; // 신청 주소
    private String startDate; // 신청 시작 날짜
    private String endDate; // 신청 종료 날짜

    @ElementCollection
    private List<Float> embedding;

    @Enumerated(EnumType.STRING)
    private ScholarshipProgressStatus progressStatus;

    private int viewCount = 0;

    /**
     * db에 저장된 방법으로 날짜 계산
     * csv 파일로 변환하기 전에 excel에서 날짜 형식 yyyy-mm-dd 형식으로 변환하는 작업 필요
     */
    public void updateProgressStatus() {
        LocalDate today = LocalDate.now();
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        if (today.isBefore(start)) {
            this.progressStatus = ScholarshipProgressStatus.UPCOMING;
        } else if (!today.isAfter(end)) { // today >= start && today <= end
            this.progressStatus = ScholarshipProgressStatus.ONGOING;
        } else {
            this.progressStatus = ScholarshipProgressStatus.ENDED;
        }
    }

    /**
     * 날짜 형식을 계산하기 전에 변환
     */
    public void updateProgressStatusConvert() {
        LocalDate today = LocalDate.now();

        // CSV에서 들어온 날짜 형식 지정 (M/d/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

        try {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            if (today.isBefore(start)) {
                this.progressStatus = ScholarshipProgressStatus.UPCOMING;
            } else if (!today.isAfter(end)) { // today >= start && today <= end
                this.progressStatus = ScholarshipProgressStatus.ONGOING;
            } else {
                this.progressStatus = ScholarshipProgressStatus.ENDED;
            }
        } catch (DateTimeParseException e) {
            System.err.println("⚠ 날짜 변환 오류: " + e.getMessage());
            this.progressStatus = ScholarshipProgressStatus.ENDED; // 기본값 설정
        }
    }

    public void addViewCount() {
        this.viewCount++;
    }

    @Builder
    public Scholarship(String name, String organizationType, String productType, String startDate, String endDate, String organizationName, String financialAidType, String universityType, String gradeType, String departmentType, String gradeRequirement, String incomeRequirement, String supportDetails, String specialQualification, String residencyRequirement, String selectionMethod, Integer selectionCount, String eligibilityRestriction, Boolean recommendationRequired, String submitDocumentDetail, String scholarshipUrl) {
        this.name = name;
        this.organizationType = organizationType;
        this.productType = productType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.organizationName = organizationName;
        this.financialAidType = financialAidType;
        this.universityType = universityType;
        this.gradeType = gradeType;
        this.departmentType = departmentType;
        this.gradeRequirement = gradeRequirement;
        this.incomeRequirement = incomeRequirement;
        this.supportDetails = supportDetails;
        this.specialQualification = specialQualification;
        this.residencyRequirement = residencyRequirement;
        this.selectionMethod = selectionMethod;
        this.selectionCount = selectionCount;
        this.eligibilityRestriction = eligibilityRestriction;
        this.recommendationRequired = recommendationRequired;
        this.submitDocumentDetail = submitDocumentDetail;
        this.scholarshipUrl = scholarshipUrl;
    }
}
