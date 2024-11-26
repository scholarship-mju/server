package mju.scholarship.scholoarship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AllScholarshipResponse {

    private Long id;

    /**
     * 장학금 관련 데이터
     */
    private String name;
    private String description;

    /**
     * 장학금 조건 데이터
     */
    private Integer price;
    private String category;
    private String university;
    private Integer minAge;
    private Integer maxAge;
    private String gender; //enum 으로 할까
    private String province;
    private String city;
    private String department; // major -> 전공 (?)
    private Double grade; // 1.0 - 4.5
    private Integer incomeQuantile; // 1-10
    private boolean isInterested;


    @Builder
    public AllScholarshipResponse(Long id, String name, String description, Integer price, String category, String university, Integer minAge, Integer maxAge, String gender, String province, String city, String department, Double grade, Integer incomeQuantile, boolean isInterested) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.university = university;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.gender = gender;
        this.province = province;
        this.city = city;
        this.department = department;
        this.grade = grade;
        this.incomeQuantile = incomeQuantile;
        this.isInterested = isInterested;
    }
}
