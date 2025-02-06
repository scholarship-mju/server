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

    private String name; // 장학금 이름
    @Column(length = 1000)
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
    private int viewCount = 0;
    private String scholarshipUrl;

    @Enumerated(EnumType.STRING)
    private ScholarshipProgressStatus progressStatus;

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
    public Scholarship(Long id, String name, String detailEligibility, String price, String university, Integer minAge, Integer maxAge, String gender, String startDate, String endDate, String submission, String province, String city, String department, Double grade, Integer incomeQuantile, int minSemester, String scholarshipUrl) {
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
        this.minSemester = minSemester;
        this.scholarshipUrl = scholarshipUrl;
    }
}
