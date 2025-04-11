package mju.scholarship.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ScholarshipCrawlingRequest {

    private String organizationName;
    private String name;
    private String organizationType;
    private String productType;
    private String financialAidType;
    private String universityType;
    private String gradeType;
    private String departmentType;
    private String gradeRequirement;
    private String incomeRequirement;
    private String supportDetails;
    private String specialQualification;
    private String residencyRequirement;
    private String selectionMethod;
    private Integer selectionCount;
    private String eligibilityRestriction;
    private Boolean recommendationRequired;
    private String submitDocumentDetail;
    private String scholarshipUrl;
    private String startDate;
    private String endDate;
}
