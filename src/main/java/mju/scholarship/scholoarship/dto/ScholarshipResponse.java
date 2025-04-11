package mju.scholarship.scholoarship.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import mju.scholarship.scholoarship.ScholarshipProgressStatus;

@Getter
public class ScholarshipResponse {

    private Long id;
    private String name; // 장학금 이름
    private String organizationType;
    private String productType; // 상품구분
    private String startDate; // 신청 시작 날짜
    private String endDate; // 신청 종료 날짜
    private String organizationName; // 운영기관명
    private String financialAidType; // 학자금유형구분
    private String universityType; // 대학구분
    private String gradeType; // 학년구분
    private String departmentType; // 학과구분
    private String gradeRequirement; // 성적기준
    private String incomeRequirement; // 소득기준
    private String supportDetails; // 지원내역
    private String specialQualification; // 특정자격
    private String residencyRequirement; // 지역거주여부
    private String selectionMethod; // 선발방법
    private String selectionCount; // 선발인원
    private String eligibilityRestriction; // 자격제한
    private String recommendationRequired; // 추천필요여부
    private String  submitDocumentDetail; //제출 서류 상세 내용
    private String scholarshipUrl;
    private ScholarshipProgressStatus progressStatus;
    private int viewCount;
    private String scholarshipImage;

    @Builder
    public ScholarshipResponse(String scholarshipImage, int viewCount, String productType, String organizationType, Long id, String name, String startDate, String endDate, String organizationName, String financialAidType, String universityType, String gradeType, String departmentType, String gradeRequirement, String incomeRequirement, String supportDetails, String specialQualification, String residencyRequirement, String selectionMethod, String selectionCount, String eligibilityRestriction, String recommendationRequired, String submitDocumentDetail, String scholarshipUrl, ScholarshipProgressStatus progressStatus) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.productType = productType;
        this.organizationType = organizationType;
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
        this.progressStatus = progressStatus;
        this.viewCount = viewCount;
        this.scholarshipImage = scholarshipImage;
    }
}
