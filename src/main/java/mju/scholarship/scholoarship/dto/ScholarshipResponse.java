package mju.scholarship.scholoarship.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import mju.scholarship.scholoarship.ScholarshipProgressStatus;

@Getter
public class ScholarshipResponse {

    private Long id;
    private String name; // 장학금 이름
    private String detailEligibility; //신청 자격
    private String price; //
    private String university; // 대학교
    private Integer minAge; // 최소 나이
    private Integer maxAge; // 최대 나이
    private String gender; // 성별
    private String startDate; // 신청 시작 날짜
    private String endDate; // 신청 종료 날짜
    private String submission; // 제출 파일
    private String province; // 도/광역시
    private String city; // 시
    private String department; // 학과
    private Double grade; // 학점
    private Integer incomeQuantile; // 소득분위
    private ScholarshipProgressStatus progressStatus; // 장학금 신청 상태
    private int minSemester;
    private int viewCount;
    private String scholarshipUrl;

    @Builder
    public ScholarshipResponse(Long id, String name, String detailEligibility, String price, String university, Integer minAge, Integer maxAge, String gender, String startDate, String endDate, String submission, String province, String city, String department, Double grade, Integer incomeQuantile, ScholarshipProgressStatus progressStatus, int minSemester, int viewCount, String scholarshipUrl) {
        this.id = id;
        this.name = name;
        this.detailEligibility = detailEligibility;
        this.price = price;
        this.university = university;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.gender = gender;
        this.startDate = startDate;
        this.endDate = endDate;
        this.submission = submission;
        this.province = province;
        this.city = city;
        this.department = department;
        this.grade = grade;
        this.incomeQuantile = incomeQuantile;
        this.progressStatus = progressStatus;
        this.minSemester = minSemester;
        this.viewCount = viewCount;
        this.scholarshipUrl = scholarshipUrl;
    }
}
