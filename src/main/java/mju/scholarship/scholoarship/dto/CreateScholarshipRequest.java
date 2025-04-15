package mju.scholarship.scholoarship.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.ScholarshipProgressStatus;

@Getter
@NoArgsConstructor
public class CreateScholarshipRequest {

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
    private int minSemester; // 최소 학기
    private String scholarshipUrl; // 관련 사이트 url
}
