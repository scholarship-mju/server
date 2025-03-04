package mju.scholarship.scholoarship;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @Enumerated(EnumType.STRING)
    private ScholarshipProgressStatus progressStatus;

    private int viewCount = 0;

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
